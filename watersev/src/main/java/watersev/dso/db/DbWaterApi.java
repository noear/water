package watersev.dso.db;

import watersev.Config;
import watersev.models.water.MonitorModel;
import watersev.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by noear on 2017/7/27.
 */
public class DbWaterApi {


    //获取同步任务列表
    public static List<SynchronousModel> getSyncList() {
        try {
            return Config.water.table("water_tool_synchronous")
                    .where("is_enabled = 1")
                    .select("*")
                    .getList(new SynchronousModel());
        } catch (Exception ex) {
            ex.printStackTrace();

            return new ArrayList<>();
        }
    }

    public static void setSyncTaskTag(int sync_id, long task_tag) throws SQLException {
        Config.water.table("water_tool_synchronous")
                .set("task_tag", task_tag)
                .where("sync_id=?", sync_id).update();
    }

    public static void setSyncLastTime(int sync_id) throws SQLException {
        Config.water.table("water_tool_synchronous")
                .set("last_fulltime", new Date())
                .where("sync_id=?", sync_id).update();
    }


    public static List<MonitorModel> getMonitorList() {
        try {
            return Config.water.table("water_tool_monitor")
                    .where("is_enabled=1")
                    .select("*")
                    .getList(new MonitorModel());
        } catch (Exception ex) {
            ex.printStackTrace();

            return new ArrayList<>();
        }
    }

    public static void setMonitorState(int monitor_id, int alarm_count, String task_tag) throws SQLException {
        Config.water.table("water_tool_monitor")
                .set("alarm_count", alarm_count)
                .set("task_tag", task_tag)
                .where("monitor_id=?", monitor_id).update();
    }


}
