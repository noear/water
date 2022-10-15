package wateradmin.dso.db;

import org.noear.water.utils.IDUtils;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_tool.ReportModel;
import wateradmin.models.water_tool.MonitorModel;
import wateradmin.models.water_tool.SynchronousModel;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DbWaterApi {
    private static DbContext db() {
        return Config.water;
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