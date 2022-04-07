package wateradmin.dso.db;

import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.I18nModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2022/04/07 created
 */
public class DbWaterCfgI18nApi {
    private static DbContext db() {
        return Config.water;
    }

    //获取白名单表tag
    public static List<TagCountsModel> getI18nTags() throws SQLException {
        return db().table("water_cfg_i18n")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    //获取ip白名单列表
    public static List<I18nModel> getI18nListByTag(String tag_name, String name, int state) throws SQLException {
        return db().table("water_cfg_i18n")
                .whereEq("is_enabled", state == 1)
                .build(tb -> {
                    if (tag_name != null) {
                        tb.andEq("tag", tag_name);
                    }

                    if (TextUtils.isEmpty(name) == false) {
                        tb.andLk("name", name + "%");
                    }
                })
                .select("*")
                .getList(I18nModel.class);
    }

    //新增ip白名单
    public static boolean setI18n(Integer i18n_id, String tag, String bundle, String lang ,String name, String value) throws SQLException {
        if (i18n_id == null) {
            i18n_id = 0;
        }

        if (value == null) {
            return false;
        }

        DbTableQuery qr = db().table("water_cfg_i18n")
                .set("tag", tag.trim())
                .set("bundle", bundle.trim())
                .set("lang", lang.trim())
                .set("name", name.trim())
                .set("value", value.trim())
                .set("gmt_modified", System.currentTimeMillis());

        if (i18n_id > 0) {
            qr.whereEq("i18n_id", i18n_id).update();
        } else {
            qr.insert();
        }

        return true;
    }

    //批量导入
    public static void impI18n(String tag, I18nModel wm) throws SQLException {
        if (TextUtils.isEmpty(tag) == false) {
            wm.tag = tag;
        }

        if (TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.value)) {
            return;
        }

        db().table("water_cfg_i18n")
                .set("tag", tag.trim())
                .set("bundle", wm.bundle)
                .set("lang", wm.lang)
                .set("name", wm.name)
                .set("value", wm.value)
                .set("gmt_modified", System.currentTimeMillis())
                .insertBy("tag,bundle,lang,name");
    }

    //删除
    public static boolean delI18n(int row_id) throws SQLException {
        return db().table("water_cfg_i18n")
                .where("row_id = ?", row_id)
                .delete() > 0;
    }

    //批量删除
    public static void delI18nByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (act == 9) {
            db().table("water_cfg_i18n")
                    .whereIn("row_id", list)
                    .delete();
        } else {
            db().table("water_cfg_i18n")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .set("gmt_modified", System.currentTimeMillis())
                    .whereIn("row_id", list)
                    .update();
        }
    }

    public static I18nModel getI18n(int row_id) throws SQLException {
        return db().table("water_cfg_i18n")
                .where("row_id = ?", row_id)
                .selectItem("*", I18nModel.class);
    }

    public static List<I18nModel> getI18nByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("water_cfg_i18n")
                .whereIn("row_id", list)
                .selectList("*", I18nModel.class);
    }
}
