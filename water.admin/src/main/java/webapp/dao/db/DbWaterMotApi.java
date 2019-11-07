package webapp.dao.db;

import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.Config;
import webapp.models.water.MonitorModel;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DbWaterMotApi {
    private static DbContext db() {
        return Config.water_log;
    }

    //编辑更新监视任务。
    public static boolean setMonitor(Integer monitor_id, String tag, String name, Integer type, String source, String source_model, String rule, String task_tag_exp, String alarm_mobile, String alarm_sign, String alarm_exp, Integer is_enabled) throws SQLException {
        String guid = UUID.randomUUID().toString().replaceAll("-", "");

        DbTableQuery db = db().table("water_base_monitor")
                .set("key", guid)
                .set("name", name)
                .set("tag", tag)
                .set("type", type)
                .set("source", source)
                .set("source_model", source_model)
                .set("rule", rule)
                .set("task_tag_exp", task_tag_exp)
                .set("alarm_mobile", alarm_mobile)
                .set("alarm_sign",alarm_sign)
                .set("alarm_exp", alarm_exp)
                .set("is_enabled", is_enabled);

        if (monitor_id > 0) {
            return db.where("id = ?", monitor_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }

    //获取monitor表的tag分组信息。
    public static List<MonitorModel> getMonitorTags() throws SQLException {
        return db().table("water_base_monitor")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(new MonitorModel());
    }


    //获取monitor表中的数据。
    public static List<MonitorModel> getMonitorList(String tag_name, String monitor_name, Integer _state) throws SQLException {
        return db()
                .table("water_base_monitor")
                .where("is_enabled = ?", _state)
                .and("tag = ?", tag_name)
                .expre(tb -> {
                    if (!TextUtils.isEmpty(monitor_name))
                        tb.and("name like ?", monitor_name + "%");
                })
                .select("*")
                .getList(new MonitorModel());
    }

    //根据id查找对应monitor，用于编辑功能。
    public static MonitorModel getMonitorById(Integer monitor_id) throws SQLException {
        return db()
                .table("water_base_monitor")
                .where("id = ?", monitor_id)
                .select("*")
                .getItem(new MonitorModel());
    }
}
