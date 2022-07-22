package watersev.dso.db;

import lombok.extern.slf4j.Slf4j;
import org.noear.weed.DbContext;
import watersev.Config;
import watersev.models.water_tool.CertificationModel;
import watersev.models.water_tool.DetectionModel;
import watersev.models.water_tool.MonitorModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2022/7/22 created
 */
@Slf4j
public class DbWaterToolApi {
    public static DbContext db() {
        return Config.water;
    }


    public static List<MonitorModel> getMonitorList() {
        try {
            return db().table("water_tool_monitor")
                    .where("is_enabled=1")
                    .selectList("*", MonitorModel.class);
        } catch (Exception ex) {
            log.error("{}", ex);

            return new ArrayList<>();
        }
    }

    public static void setMonitorState(int monitor_id, int alarm_count, String task_tag) throws SQLException {
        db().table("water_tool_monitor")
                .set("alarm_count", alarm_count)
                .set("task_tag", task_tag)
                .where("monitor_id=?", monitor_id)
                .update();
    }


    public static List<DetectionModel> detectionGetList() throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_tool_detection")
                .where("is_enabled=1")
                .selectList("*", DetectionModel.class);
    }

    /**
     * 更新服务，同时修改检测时间和备注（服务是被动检测的）
     */
    public static void detectionSetState(long detection_id, int check_state, String check_note) {
        try {
            db().table("water_tool_detection").usingExpr(true)
                    .set("state", 0)
                    .set("check_last_state", check_state)
                    .set("check_last_time", System.currentTimeMillis())
                    .set("check_last_note", check_note)
                    .build((tb) -> {
                        if (check_state == 0)
                            tb.set("check_error_num", 0);
                        else
                            tb.set("check_error_num", "$check_error_num+1");
                    })
                    .whereEq("detection_id", detection_id)
                    .update();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }

    public static List<CertificationModel> certificationGetList() throws SQLException {
        //不能缓存（以便随时获取状态）
        return db().table("water_tool_certification")
                .where("is_enabled=1")
                .selectList("*", CertificationModel.class);
    }

    /**
     * 更新服务，同时修改检测时间和备注（服务是被动检测的）
     */
    public static void certificationSetState(long certification_id, int check_state, String check_note, Date timeOfEnd) {
        try {
            db().table("water_tool_certification").usingExpr(true)
                    .set("state", 0)
                    .set("check_last_state", check_state)
                    .set("check_last_time", System.currentTimeMillis())
                    .set("check_last_note", check_note)
                    .build(tb -> {
                        if (timeOfEnd != null) {
                            tb.set("time_of_end", timeOfEnd);
                        }
                    })
                    .whereEq("certification_id", certification_id)
                    .update();
        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }
}
