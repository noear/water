package wateradmin.dso.db;

import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.ReportModel;
import wateradmin.models.water.MonitorModel;
import wateradmin.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.*;

public class DbWaterApi {
    private static DbContext db() {
        return Config.water;
    }


    //获取monitor表中的数据。
    public static List<MonitorModel> monitorGetList(String tag_name, String monitor_name, boolean is_enabled) throws SQLException {
        return db()
                .table("water_tool_monitor")
                .whereEq("is_enabled", is_enabled ? 1 : 0)
                .andEq("tag", tag_name)
                .build(tb -> {
                    if (!TextUtils.isEmpty(monitor_name)) {
                        tb.andLk("name", monitor_name + "%");
                    }
                })
                .selectList("*", MonitorModel.class);
    }

    //根据id查找对应monitor，用于编辑功能。
    public static MonitorModel monitorGet(int monitor_id) throws SQLException {
        if (monitor_id == 0) {
            return new MonitorModel();
        }

        return db()
                .table("water_tool_monitor")
                .where("monitor_id = ?", monitor_id)
                .selectItem("*", MonitorModel.class);
    }


    //编辑更新监视任务。
    public static boolean monitorSave(Integer monitor_id, String tag, String name, Integer type, String source_query, String rule, String task_tag_exp, String alarm_mobile, String alarm_sign, String alarm_exp, Integer is_enabled) throws SQLException {
        String guid = UUID.randomUUID().toString().replaceAll("-", "");

        DbTableQuery db = db().table("water_tool_monitor")
                .set("key", guid)
                .set("name", name)
                .set("tag", tag)
                .set("type", type)
                .set("source_query", source_query)
                .set("rule", rule)
                .set("task_tag_exp", task_tag_exp)
                .set("alarm_mobile", alarm_mobile)
                .set("alarm_sign", alarm_sign)
                .set("alarm_exp", alarm_exp)
                .set("is_enabled", is_enabled);

        if (monitor_id > 0) {
            return db.whereEq("monitor_id", monitor_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }

    public static boolean monitorDel(Integer monitor_id) throws SQLException {
        return db().table("water_tool_monitor")
                .whereEq("monitor_id", monitor_id)
                .delete() > 0;
    }


    //获取monitor表的tag分组信息。
    public static List<TagCountsModel> monitorGetTags() throws SQLException {
        return db().table("water_tool_monitor")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }


    //根据名称和状态列出同步列表。
    public static List<SynchronousModel> syncGetList(String tag_name, String name, int _state) throws SQLException {
        return db()
                .table("water_tool_synchronous")
                .where("is_enabled = ?", _state)
                .and("tag = ?", tag_name)
                .build(tb -> {
                    if (!TextUtils.isEmpty(name))
                        tb.and("name like ?", name + "%");
                })
                .selectList("*", SynchronousModel.class);
    }

    public static boolean syncAdd(Integer type, String name, Integer interval, String target, String target_pk, String source,
                                  String source_model, String alarm_mobile, Integer is_enabled) throws SQLException {
        return db().table("water_tool_synchronous")
                .set("key", UUID.randomUUID().toString().replaceAll("-", ""))
                .set("name", name)
                .set("type", type)
                .set("interval", interval)
                .set("target", target)
                .set("target_pk", target_pk)
                .set("source", source)
                .set("source_model", source_model)
                .set("alarm_mobile", alarm_mobile)
                .set("is_enabled", is_enabled)
                .insert() > 0;
    }


    //数据同步  根据id获得对象
    public static SynchronousModel syncGet(int sync_id) throws SQLException {
        if (sync_id == 0) {
            return new SynchronousModel();
        }

        return db().table("water_tool_synchronous")
                .where("sync_id = ?", sync_id)
                .limit(1)
                .selectItem("*", SynchronousModel.class);
    }

    public static boolean syncSave(Integer sync_id, Integer type, String name, String tag, Integer interval, String target, String target_pk,
                                   String source_model, String alarm_mobile, Integer is_enabled) throws SQLException {
        DbTableQuery db = db().table("water_tool_synchronous")
                .set("name", name)
                .set("tag", tag)
                .set("type", type)
                .set("interval", interval)
                .set("target", target)
                .set("target_pk", target_pk)
                .set("source_model", source_model)
                .set("alarm_mobile", alarm_mobile)
                .set("is_enabled", is_enabled);

        if (sync_id > 0) {
            return db.where("sync_id = ?", sync_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }

    public static boolean syncDel(Integer sync_id) throws SQLException {
        return db().table("water_tool_synchronous")
                .whereEq("sync_id", sync_id)
                .delete() > 0;
    }

    //获取synchronous表的tag分组信息。
    public static List<TagCountsModel> syncGetTags() throws SQLException {
        return db().table("water_tool_synchronous")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }


    public static List<TagCountsModel> reportGetTags() throws SQLException {
        return db().table("water_tool_report")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    public static List<ReportModel> reportGetListByTag(String tag) throws SQLException {
        return db().table("water_tool_report")
                .where("tag = ?", tag)
                .selectList("*", ReportModel.class);
    }

    public static ReportModel reportGet(int row_id) throws SQLException {
        return db().table("water_tool_report")
                .where("row_id = ?", row_id)
                .selectItem("*", ReportModel.class);
    }

    public static boolean reportSave(int row_id, String tag, String name, String code, String note, String args) throws SQLException {
        DbTableQuery dq = db().table("water_tool_report")
                .set("tag", tag)
                .set("name", name)
                .set("note", note)
                .set("args", args)
                .set("code", code);

        if (row_id > 0) {
            //update
            return dq.where("row_id = ?", row_id)
                    .update() > 0;
        } else {
            //add
            return dq.set("create_fulltime", new Date())
                    .insert() > 0;
        }
    }

    public static boolean reportDel(int row_id) throws SQLException {
        return db().table("water_tool_report")
                .whereEq("row_id", row_id)
                .delete() > 0;
    }
}