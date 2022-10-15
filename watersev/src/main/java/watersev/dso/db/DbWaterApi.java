package watersev.dso.db;

import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import watersev.Config;
import watersev.models.water_tool.MonitorModel;
import watersev.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by noear on 2017/7/27.
 */
@Slf4j
public class DbWaterApi {
    public static DbContext db() {
        return Config.water;
    }

    //获取同步任务列表
    public static List<SynchronousModel> getSyncList() {
        try {
            return db().table("water_tool_synchronous")
                    .where("is_enabled = 1")
                    .selectList("*", SynchronousModel.class);
        } catch (Exception ex) {
            log.error("{}", ex);

            return new ArrayList<>();
        }
    }

    public static void setSyncTaskTag(int sync_id, long task_tag) throws SQLException {
        db().table("water_tool_synchronous")
                .set("task_tag", task_tag)
                .where("sync_id=?", sync_id)
                .update();
    }

    public static void setSyncLastTime(int sync_id) throws SQLException {
        db().table("water_tool_synchronous")
                .set("last_fulltime", new Date())
                .where("sync_id=?", sync_id)
                .update();
    }

}
