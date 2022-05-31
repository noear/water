package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.PingUtils;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterDetApi;
import watersev.models.water.DetectionModel;
import watersev.utils.CallUtil;
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

        if (LockUtils.tryLock(WW.watersev_det, WW.watersev_det, 59)) {
            exec0();
        }
    }

    private void exec0() throws SQLException {
        //取出待处理的服务（已启用的服务）
        List<DetectionModel> list = DbWaterDetApi.getServiceList();

        for (DetectionModel task : list) {
            CallUtil.asynCall(() -> {
                check(task);
            });
        }
    }

    //检测服务，并尝试报警
    private void check(DetectionModel task) {
        String threadName = "det-" + task.detection_id;
        Thread.currentThread().setName(threadName);

        //被动检测模式
        check_type0(task);
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
        String detName = "det_" + sev.detection_id;

        try {
            URI uri = URI.create(url);

            //ping 检测
            long time_start = System.currentTimeMillis();
            PingUtils.ping(uri.getAuthority(), 2000);
            long time_span = System.currentTimeMillis() - time_start;

            DbWaterDetApi.udpService0(sev.detection_id, 0, "");
            TrackBuffer.singleton().append("_waterdet", "app", detName, time_span);

            if (sev.check_error_num > 0) {
                AlarmUtil.tryAlarm(sev, true, 200);
            }
        } catch (Throwable ex) {
            DbWaterDetApi.udpService0(sev.detection_id, 1, "0");
            LogUtil.sevWarn(getName(), sev.detection_id + "", sev.name + "@" + sev.address + "::\n" + Utils.throwableToString(ex));

            if (LockUtils.tryLock(WW.watersev_det, "det-a-" + sev.detection_id, 30)) {
                AlarmUtil.tryAlarm(sev, false, 0);
            }
        }
    }

    private void check_type0_http(DetectionModel sev, String url) {
        String detName = "det_" + sev.detection_id;

        try {
            /**
             * callback:
             * isOk:请求是否成功
             * code:如果成功，状态码为何?
             * hint:如果出错，提示信息?
             */

            String url2 = url;
            long time_start = System.currentTimeMillis();

            //最长5秒会回调
            HttpUtilEx.getStatusByAsync(url, (isOk, code, hint) -> {
                long time_span = System.currentTimeMillis() - time_start;
                Thread.currentThread().setName("sev-c-" + sev.detection_id);

                if (code >= 200 && code < 400) { //正常
                    DbWaterDetApi.udpService0(sev.detection_id, 0, code + "");

                    TrackBuffer.singleton().append("_waterdet", "app", detName, time_span);

                    if (sev.check_error_num > 0) {
                        AlarmUtil.tryAlarm(sev, true, code);
                    }
                } else {
                    TrackBuffer.singleton().append("_waterdet", "app", detName, time_span);

                    DbWaterDetApi.udpService0(sev.detection_id, 1, code + "");
                    LogUtil.sevWarn(getName(), sev.detection_id + "", sev.name + "@" + sev.address + "\n" + url2 + ", " + hint);

                    if (LockUtils.tryLock(WW.watersev_det, "det-a-" + sev.detection_id, 30)) {
                        AlarmUtil.tryAlarm(sev, false, code);
                    }
                }
            });
        } catch (Throwable ex) { //出错
            DbWaterDetApi.udpService0(sev.detection_id, 1, ex.getMessage());
            LogUtil.sevWarn(getName(), sev.detection_id + "", sev.name + "@" + sev.address + "\n" + Utils.throwableToString(ex));
        }
    }
}
