package wateradmin.dso.db;

import org.noear.water.utils.IDUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water.DetectionModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear 2022/5/30 created
 */
public class DbWaterToolApi {
    private static DbContext db() {
        return Config.water;
    }

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
    public static boolean detectionSave(int detection_id, String tag, String name, String source_query, String rule, String task_tag_exp, String alarm_mobile, String alarm_sign, String alarm_exp, int is_enabled) throws SQLException {
        String guid = IDUtils.guid();

        DbTableQuery db = db().table("water_tool_detection")
                .set("key", guid)
                .set("name", name)
                .set("tag", tag)
                .set("type", 0)
                .set("source_query", source_query)
                .set("rule", rule)
                .set("task_tag_exp", task_tag_exp)
                .set("alarm_mobile", alarm_mobile)
                .set("alarm_sign", alarm_sign)
                .set("alarm_exp", alarm_exp)
                .set("is_enabled", is_enabled);

        if (detection_id > 0) {
            return db.whereEq("detection_id", detection_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }

    public static boolean detectionDel(Integer detection_id) throws SQLException {
        return db().table("water_tool_detection")
                .whereEq("detection_id", detection_id)
                .delete() > 0;
    }


    //获取detection表的tag分组信息。
    public static List<TagCountsModel> detectionGetTags() throws SQLException {
        return db().table("water_tool_detection")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }
}
