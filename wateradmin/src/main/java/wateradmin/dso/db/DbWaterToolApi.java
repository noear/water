package wateradmin.dso.db;

import org.noear.water.utils.CaUtils;
import org.noear.water.utils.IDUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_tool.CertificationModel;
import wateradmin.models.water_tool.DetectionModel;
import wateradmin.models.water_tool.MonitorModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2022/5/30 created
 */
public class DbWaterToolApi {
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
    public static boolean monitorSave(int monitor_id, String tag, String name, String source_query, String rule, String task_tag_exp, String alarm_mobile, String alarm_exp, int is_enabled) throws SQLException {
        String guid = IDUtils.guid();

        DbTableQuery db = db().table("water_tool_monitor")
                .set("key", guid)
                .set("name", name)
                .set("tag", tag)
                .set("type", 0)
                .set("source_query", source_query)
                .set("rule", rule)
                .set("task_tag_exp", task_tag_exp)
                .set("alarm_mobile", alarm_mobile)
                .set("alarm_sign", "")
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

    public static void monitorDelByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (list.size() == 0) {
            return;
        }

        if (act == 9) {
            db().table("water_tool_monitor")
                    .whereIn("monitor_id", list)
                    .delete();
        } else {
            db().table("water_tool_monitor")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .set("gmt_modified", System.currentTimeMillis())
                    .whereIn("monitor_id", list)
                    .update();
        }
    }



    //获取monitor表的tag分组信息。
    public static List<TagCountsModel> monitorGetTags() throws SQLException {
        return db().table("water_tool_monitor")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    //=======================================================

    //获取detection表中的数据。
    public static List<DetectionModel> detectionGetList(String tag_name, String detection_name, boolean is_enabled) throws SQLException {
        return db()
                .table("water_tool_detection")
                .whereEq("is_enabled", is_enabled ? 1 : 0)
                .andEq("tag", tag_name)
                .build(tb -> {
                    if (!TextUtils.isEmpty(detection_name)) {
                        tb.andLk("name", detection_name + "%");
                    }
                })
                .selectList("*", DetectionModel.class);
    }

    //根据id查找对应detection，用于编辑功能。
    public static DetectionModel detectionGet(int detection_id) throws SQLException {
        if (detection_id == 0) {
            return new DetectionModel();
        }

        return db()
                .table("water_tool_detection")
                .where("detection_id = ?", detection_id)
                .selectItem("*", DetectionModel.class);
    }


    //编辑更新监视任务。
    public static boolean detectionSave(int detection_id, String tag, String name, String protocol, String address, int check_interval, int is_enabled) throws SQLException {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            return false;
        }

        if (tag == null) {
            tag = "";
        }

        DbTableQuery query = db()
                .table("water_tool_detection")
                .set("name", name)
                .set("tag", tag)
                .set("protocol", protocol)
                .set("address", address)
                .set("check_interval", check_interval)
                .set("is_enabled", is_enabled);


        if (detection_id == 0) {
            String key = IDUtils.guid();
            query.set("check_last_time", System.currentTimeMillis())
                    .set("key", key)
                    .insert();
        } else {
            query.whereEq("detection_id", detection_id)
                    .update();
        }

        return true;
    }

    public static boolean detectionDel(Integer detection_id) throws SQLException {
        return db().table("water_tool_detection")
                .whereEq("detection_id", detection_id)
                .delete() > 0;
    }

    public static void detectionDelByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (list.size() == 0) {
            return;
        }

        if (act == 9) {
            db().table("water_tool_detection")
                    .whereIn("detection_id", list)
                    .delete();
        } else {
            db().table("water_tool_detection")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .set("gmt_modified", System.currentTimeMillis())
                    .whereIn("detection_id", list)
                    .update();
        }
    }


    //获取detection表的tag分组信息。
    public static List<TagCountsModel> detectionGetTags() throws SQLException {
        return db().table("water_tool_detection")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    //=====================


    //获取certification表中的数据。
    public static List<CertificationModel> certificationGetList(String tag_name, String url, boolean is_enabled, String sort) throws SQLException {
        return db()
                .table("water_tool_certification")
                .whereEq("is_enabled", is_enabled ? 1 : 0)
                .andEq("tag", tag_name)
                .build(tb -> {
                    if (TextUtils.isNotEmpty(url)) {
                        tb.andLk("url", "%" + url + "%");
                    }

                    if (TextUtils.isNotEmpty(sort)) {
                        tb.orderBy(sort);
                    }
                })
                .selectList("*", CertificationModel.class);
    }

    //根据id查找对应certification，用于编辑功能。
    public static CertificationModel certificationGet(int certification_id) throws SQLException {
        if (certification_id == 0) {
            return new CertificationModel();
        }

        return db()
                .table("water_tool_certification")
                .where("certification_id = ?", certification_id)
                .selectItem("*", CertificationModel.class);
    }


    //编辑更新监视任务。
    public static boolean certificationSave(int certification_id, String tag, String url, String note, int is_enabled) throws Exception {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(url)) {
            return false;
        }

        if (tag == null) {
            tag = "";
        }

        if (url.contains("://") == false) {
            url = "https://" + url;
        }

        Date time_of_end = CaUtils.getCaEndTime(url);

        DbTableQuery query = db()
                .table("water_tool_certification")
                .set("note", note)
                .set("tag", tag)
                .set("url", url)
                .set("check_interval", 0)
                .set("time_of_end", time_of_end)
                .set("is_enabled", is_enabled);


        if (certification_id == 0) {
            String key = IDUtils.guid();
            query.set("check_last_time", System.currentTimeMillis())
                    .set("key", key)
                    .insert();
        } else {
            query.whereEq("certification_id", certification_id)
                    .update();
        }

        return true;
    }

    public static boolean certificationDel(Integer certification_id) throws SQLException {
        return db().table("water_tool_certification")
                .whereEq("certification_id", certification_id)
                .delete() > 0;
    }

    public static void certificationDelByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (list.size() == 0) {
            return;
        }

        if (act == 9) {
            db().table("water_tool_certification")
                    .whereIn("certification_id", list)
                    .delete();
        } else {
            db().table("water_tool_certification")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .set("gmt_modified", System.currentTimeMillis())
                    .whereIn("certification_id", list)
                    .update();
        }
    }


    //获取certification表的tag分组信息。
    public static List<TagCountsModel> certificationGetTags() throws SQLException {
        return db().table("water_tool_certification")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }
}
