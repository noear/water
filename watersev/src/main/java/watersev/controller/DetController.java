package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.dso.GatewayUtils;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.PingUtils;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterDetApi;
import watersev.models.water.DetectionModel;
import watersev.utils.HttpUtilEx;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * 应用监视（可集群，可多实例运行。同时间，只会有一个节点有效）
 *
 * @author noear
 * */
@Component
public final class DetController implements IJob {
    @Override
    public String getName() {
        return "det";
    }

    @Override
    public int getInterval() {
        return 1000 * 60; //实际是：60s 跑一次
    }


    @Override
    public void exec() throws Throwable {
        RegController.addService("watersev-" + getName());

        //尝试获取锁（1秒内只能调度一次），避免集群切换时，多次运行
        //
        if (LockUtils.tryLock(WW.watersev_det, WW.watersev_det, 59)) {
            exec0();
        }
    }

    private void exec0() throws SQLException {
        //取出待处理的服务（已启用的服务）
        List<DetectionModel> list = DbWaterDetApi.getServiceList();

        for (DetectionModel sev : list) {
            check(sev);
        }
    }

    //检测服务，并尝试报警
    private void check(DetectionModel sev) {
        Thread.currentThread().setName("det-" + sev.detection_id);

        //被动检测模式
        check_type0(sev);
    }


    /**
     * 被动检测模式
     */
    private void check_type0(DetectionModel sev) {
        String url = sev.protocol + "://" + sev.address;


        if (url.startsWith("http://") || url.startsWith("https://")) {
            check_type0_http(sev, url);
        }

        if (url.startsWith("tcp://")) {
            check_type0_tcp(sev, url);
        }
    }

    private void check_type0_tcp(DetectionModel sev, String url) {
        String nameAndIp = sev.name + "@" + sev.address;

        try {
            URI uri = URI.create(url);

            DbWaterDetApi.setServiceState(sev.detection_id, 1);//设为;正在处理中

            //ping 检测
            PingUtils.ping(uri.getAuthority(), 2000);

            DbWaterDetApi.udpService0(sev.detection_id, 0, "");

            TrackBuffer.singleton().appendCount("_waterdet", "app", nameAndIp, 1, 0);

        } catch (Throwable ex) {
            TrackBuffer.singleton().appendCount("_waterdet", "app", nameAndIp, 1, 1);


            DbWaterDetApi.udpService0(sev.detection_id, 1, "0");
            LogUtil.sevWarn(getName(), sev.address, sev.name + "@" + sev.address + "::\n" + Utils.throwableToString(ex));
        }
    }

    private void check_type0_http(DetectionModel sev, String url) {
        String nameAndIp = sev.name + "@" + sev.address;

        try {
            DbWaterDetApi.setServiceState(sev.detection_id, 1);//设为;正在处理中

            /**
             * callback:
             * isOk:请求是否成功
             * code:如果成功，状态码为何?
             * hint:如果出错，提示信息?
             */

            String url2 = url;

            //最长5秒会回调
            HttpUtilEx.getStatusByAsync(url, (isOk, code, hint) -> {
                Thread.currentThread().setName("sev-c-" + sev.detection_id);

                if (code >= 200 && code < 400) { //正常
                    DbWaterDetApi.udpService0(sev.detection_id, 0, code + "");


                    TrackBuffer.singleton().appendCount("_waterdet", "app", nameAndIp, 1, 0);

                    if (sev.check_error_num >= 2) { //之前2次坏的，现在好了提示一下
                        AlarmUtil.tryAlarm(sev, true, code);
                        //通知给网关
                        GatewayUtils.notice(sev.tag, sev.name);
                    }
                } else {
                    TrackBuffer.singleton().appendCount("_waterdet", "app", nameAndIp, 1, 1);


                    DbWaterDetApi.udpService0(sev.detection_id, 1, code + "");
                    LogUtil.sevWarn(getName(), sev.address, sev.name + "@" + sev.address + "\n" + url2 + ", " + hint);

                    if (sev.check_error_num >= 2) {//之前好的，现在坏了提示一下
                        //报警，30秒一次
                        //
                        if (LockUtils.tryLock(WW.watersev_sevchk, "sev-a-" + sev.detection_id, 30)) {
                            AlarmUtil.tryAlarm(sev, false, code);
                        }

                        if (sev.check_error_num == 2) {
                            GatewayUtils.notice(sev.tag, sev.name);
                        }
                    }
                }
            });
        } catch (Throwable ex) { //出错
            TrackBuffer.singleton().appendCount("_waterdet", "app", nameAndIp, 1, 1);

            DbWaterDetApi.udpService0(sev.detection_id, 1, ex.getMessage());
            LogUtil.sevWarn(getName(), sev.address, sev.name + "@" + sev.address + "\n" + Utils.throwableToString(ex));
        }
    }
}
