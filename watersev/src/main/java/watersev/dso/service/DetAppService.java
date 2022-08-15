package watersev.dso.service;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.PingUtils;
import org.noear.water.utils.RunUtils;
import org.noear.water.utils.Timespan;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterToolApi;
import watersev.models.water_tool.DetectionModel;
import watersev.utils.HttpUtilEx;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * @author noear 2022/7/25 created
 */
@Component
public class DetAppService {
    public String getName() {
        return "detapp";
    }

    public void execDo() throws SQLException {
        //取出待处理的服务（已启用的服务）
        List<DetectionModel> list = DbWaterToolApi.detectionGetList();

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

            try {
                check(task);
            }catch (Throwable e){
                String trackName = task.name + "@" + task.protocol + "://" + task.address;

                LogUtil.warn(getName(), task.detection_id + "", trackName + "\n" + Utils.throwableToString(e));
            }
        }
    }

    //检测服务，并尝试报警
    private void check(DetectionModel task) {
        String threadName = getName() + "-" + task.detection_id;
        Thread.currentThread().setName(threadName);

        //检测开始
        String url = task.protocol + "://" + task.address;

        if (url.startsWith("http://") || url.startsWith("https://")) {
            check_type0_http(task, url);
            return;
        }

        if (url.contains("://")) {
            RunUtils.runAsyn(() -> {
                check_type0_tcp(task, url);
            });
            return;
        }
    }

    private void check_type0_tcp(DetectionModel task, String url) {
        String trackName = task.name + "@" + task.protocol + "://" + task.address;

        try {
            URI uri = URI.create(url);

            //ping 检测
            long time_start = System.currentTimeMillis();
            PingUtils.ping(uri.getAuthority(), 3000);
            long time_span = System.currentTimeMillis() - time_start;

            DbWaterToolApi.detectionSetState(task.detection_id, 0, "");
            WaterClient.Track.addMeterAndMd5("_waterdet", task.tag, trackName, time_span);

            if (task.check_error_num > 0) {
                AlarmUtil.tryAlarm(task, true, 200);
            }
        } catch (Throwable ex) {
            DbWaterToolApi.detectionSetState(task.detection_id, 1, "0");
            LogUtil.warn(getName(), task.detection_id + "", trackName + "::\n" + Utils.throwableToString(ex));

            if (LockUtils.tryLock(WW.watersev_det, "det-a-" + task.detection_id, 30)) {
                AlarmUtil.tryAlarm(task, false, 0);
            }
        }
    }

    private void check_type0_http(DetectionModel task, String url) {
        String trackName = task.name + "@" + task.protocol + "://" + task.address;

        try {
            /**
             * callback:
             * isOk:请求是否成功
             * code:如果成功，状态码为何?
             * hint:如果出错，提示信息?
             */
            long time_start = System.currentTimeMillis();

            //最长5秒会回调
            HttpUtilEx.getStatusByAsync(url, (isOk, code, hint) -> {
                long time_span = System.currentTimeMillis() - time_start;
                Thread.currentThread().setName("det-c-" + task.detection_id);

                if (code >= 200 && code < 400) { //正常
                    DbWaterToolApi.detectionSetState(task.detection_id, 0, code + "");

                    WaterClient.Track.addMeterAndMd5("_waterdet", task.tag, trackName, time_span);

                    if (task.check_error_num > 0) {
                        AlarmUtil.tryAlarm(task, true, code);
                    }
                } else {
                    WaterClient.Track.addMeterAndMd5("_waterdet", task.tag, trackName, time_span);

                    DbWaterToolApi.detectionSetState(task.detection_id, 1, code + "");
                    LogUtil.warn(getName(), task.detection_id + "", trackName + "\ncode=" + code + ", " + hint);

                    if (LockUtils.tryLock(WW.watersev_det, "det-a-" + task.detection_id, 30)) {
                        AlarmUtil.tryAlarm(task, false, code);
                    }
                }
            });
        } catch (Throwable ex) { //出错
            DbWaterToolApi.detectionSetState(task.detection_id, 1, ex.getMessage());
            LogUtil.warn(getName(), task.detection_id + "", trackName + "\n" + Utils.throwableToString(ex));
        }
    }
}
