package wateradmin.dso.db;

import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.I18nModel;

import java.sql.SQLException;
import java.util.ArrayList;
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
    public static List<I18nModel> getI18nListByTag(String tag, String bundle,String name, String lang) throws SQLException {
        if (lang == null) {
            lang = "";
        }

        if ("default".equals(lang)) {
            lang = "";
        }

        if (TextUtils.isEmpty(bundle)) {
            return new ArrayList<>();
        }

        return db().table("water_cfg_i18n")
                .whereEq("tag", tag)
                .andEq("bundle", bundle)
                .andEq("lang", lang)
                .build((tb) -> {
                    if (name != null) {
                        tb.andEq("name", name);
                    }
                })
                .orderBy("name ASC")
                .selectList("*", I18nModel.class);
    }


    public static List<TagCountsModel> getI18nLangsByBundle(String tag, String bundle) throws SQLException {
        if (TextUtils.isEmpty(bundle)) {
            return new ArrayList<>();
        }

        return db().table("water_cfg_i18n")
                .whereEq("tag", tag)
                .andEq("bundle", bundle)
                .groupBy("lang")
                .orderBy("lang ASC")
                .select("lang tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    public static List<TagCountsModel> getI18nBundles(String tag) throws SQLException {
        return db().table("water_cfg_i18n")
                .whereEq("tag", tag)
                .groupBy("bundle")
                .orderBy("bundle ASC")
                .select("bundle tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    public static boolean setI18n(String tag, String bundle, String name, String nameOld, String lang , String value) throws SQLException {
        value = value.replace("\\\\", "\\");
        value = value.replace("\\n", "\n");

        DbTableQuery tb = db().table("water_cfg_i18n")
                .set("tag", tag)
                .set("bundle", bundle)
                .set("name", name)
                .set("lang", lang)
                .set("value", value);

        boolean isOk = true;

        if (TextUtils.isNotEmpty(nameOld) && tb.whereEq("tag", tag)
                .andEq("bundle", bundle)
                .andEq("name", nameOld)
                .andEq("lang", lang)
                .selectExists()) {
            isOk = tb.update() > 0;
        } else {
            isOk = tb.insert() > 0;
        }

        return isOk;
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

    public static boolean delI18n(String tag ,String bundle, String name) throws SQLException {
        DbTableQuery tb = db().table("water_cfg_i18n")
                .whereEq("tag", tag)
                .andEq("bundle", bundle)
                .andEq("name", name);

        boolean isOk = tb.delete() > 0;

        return isOk;
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

    public static List<I18nModel> getI18nByName(String tag,String bundle, String name) throws SQLException {
        if (TextUtils.isEmpty(bundle) || TextUtils.isEmpty(name)) {
            return new ArrayList<>();
        } else {
            return db().table("water_cfg_i18n")
                    .whereEq("tag", tag).andEq("bundle", bundle).andEq("name", name)
                    .selectList("lang,value", I18nModel.class);
        }
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
