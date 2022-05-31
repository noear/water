package watersev.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.PingUtils;
import org.noear.water.utils.Timespan;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterDetApi;
import watersev.models.water.DetectionModel;
import watersev.utils.CallUtil;
import watersev.utils.HttpUtilEx;

import java.net.URI;
import java.sql.SQLException;
import java.util.Date;
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
        return 1000 * 5; //实际是：5s 跑一次
    }


    @Override
    public void exec() throws Throwable {
        RegController.addService("watersev-" + getName());

        if (LockUtils.tryLock(WW.watersev_det, WW.watersev_det, 4)) {
            exec0();
        }
    }

    private void exec0() throws SQLException {
        //取出待处理的服务（已启用的服务）
        List<DetectionModel> list = DbWaterDetApi.getServiceList();

        for (DetectionModel task : list) {
            if (task.check_interval == 0) {
                task.check_interval = 10;
            }

            if (task.check_interval < 5) {
                task.check_interval = 5;
            }

            if (task.check_last_time != null) {
                if (new Timespan(task.check_last_time).seconds() < task.check_interval) {
                    continue;
                }
            }

            CallUtil.asynCall(() -> {
                check(task);
            });
        }
    }

    //检测服务，并尝试报警
    private void check(DetectionModel task) {
        String threadName = "det-" + task.detection_id;
        Thread.currentThread().setName(threadName);

        //检测开始
        String url = task.protocol + "://" + task.address;

        if (url.startsWith("http://") || url.startsWith("https://")) {
            check_type0_http(task, url);
        }

        if (url.startsWith("tcp://")) {
            check_type0_tcp(task, url);
        }
    }

    private void check_type0_tcp(DetectionModel sev, String url) {
        String detName = "det_" + sev.detection_id;

        try {
            URI uri = URI.create(url);

            //ping 检测
            long time_start = System.currentTimeMillis();
            PingUtils.ping(uri.getAuthority(), 3000);
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
