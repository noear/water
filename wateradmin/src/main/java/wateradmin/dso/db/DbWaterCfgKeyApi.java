package wateradmin.dso.db;

import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.KeyModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2021/11/24 created
 */
public class DbWaterCfgKeyApi {
    private static DbContext db() {
        return Config.water;
    }

    //获取白名单表tag
    public static List<TagCountsModel> getKeyTags() throws SQLException {
        return db().table("water_cfg_key")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    //获取ip白名单列表
    public static List<KeyModel> getKeyListByTag(String tag_name, String label, boolean is_enabled) throws SQLException {
        return db().table("water_cfg_key")
                .whereEq("is_enabled", is_enabled ? 1 : 0)
                .build(tb -> {
                    if (tag_name != null) {
                        tb.andEq("tag", tag_name);
                    }

                    if (TextUtils.isEmpty(label) == false) {
                        tb.andLk("label", label + "%");
                    }
                })
                .selectList("*", KeyModel.class);
    }

    //新增ip白名单
    public static boolean setKey(Integer key_id, String tag, String access_key, String access_secret_key, String access_secret_salt, String label, String description) throws SQLException {
        if (key_id == null) {
            key_id = 0;
        }

        if (access_key == null) {
            return false;
        }

        DbTableQuery qr = db().table("water_cfg_key")
                .set("tag", tag.trim())
                .set("access_key", access_key)
                .set("access_secret_key", access_secret_key)
                .set("access_secret_salt", access_secret_salt)
                .set("label", label)
                .set("description", description)
                .set("gmt_modified", System.currentTimeMillis());

        if (key_id > 0) {
            qr.whereEq("key_id", key_id).update();
        } else {
            qr.insert();
        }

        return true;
    }

    //批量导入
    public static void impKeyOrRep(String tag, KeyModel wm) throws SQLException {
        if (TextUtils.isEmpty(tag) == false) {
            wm.tag = tag;
        }

        if (TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.access_key)) {
            return;
        }

        db().table("water_cfg_key")
                .set("tag", tag.trim())
                .set("access_key", wm.access_key)
                .set("access_secret_key", wm.access_secret_key)
                .set("access_secret_salt", wm.access_secret_salt)
                .set("label", wm.label)
                .set("description", wm.description)
                .set("gmt_modified", System.currentTimeMillis())
                .upsertBy("access_key");
    }

    //删除
    public static boolean delKey(int key_id) throws SQLException {
        return db().table("water_cfg_key")
                .where("key_id = ?", key_id)
                .delete() > 0;
    }

    //批量删除
    public static void delKeyByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (act == 9) {
            db().table("water_cfg_key")
                    .whereIn("key_id", list)
                    .delete();
        } else {
            db().table("water_cfg_key")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .set("gmt_modified", System.currentTimeMillis())
                    .whereIn("key_id", list)
                    .update();
        }
    }

    public static KeyModel getKey(int key_id) throws SQLException {
        if (key_id == 0) {
            return new KeyModel();
        }

        return db().table("water_cfg_key")
                .where("key_id = ?", key_id)
                .selectItem("*", KeyModel.class);
    }

    public static List<KeyModel> getKeyByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("water_cfg_key")
                .whereIn("key_id", list)
                .selectList("*", KeyModel.class);
    }
}
