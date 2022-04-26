package wateradmin.dso.db;

import org.noear.water.WW;
import org.noear.water.dso.NoticeUtils;
import org.noear.water.dso.WhitelistApi;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.dso.CacheUtil;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.WhitelistModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2021/11/24 created
 */
public class DbWaterCfgSafeApi {
    private static DbContext db() {
        return Config.water;
    }

    //获取白名单表tag
    public static List<TagCountsModel> getWhitelistTags() throws SQLException {
        return db().table("water_cfg_whitelist")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    //获取ip白名单列表
    public static List<WhitelistModel> getWhitelistByTag(String tag_name, String key, int state) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("is_enabled", state == 1)
                .build(tb -> {
                    if (tag_name != null) {
                        tb.andEq("tag", tag_name);
                    }

                    if (TextUtils.isEmpty(key) == false) {
                        tb.andLk("value", key + "%");
                    }
                })
                .selectList("*", WhitelistModel.class);
    }

    //新增ip白名单
    public static boolean setWhitelist(Integer row_id, String tag, String type, String value, String note, int is_enabled) throws SQLException {
        if (row_id == null) {
            row_id = 0;
        }

        if (value == null) {
            return false;
        }

        DbTableQuery qr = db().table("water_cfg_whitelist")
                .set("tag", tag.trim())
                .set("type", type.trim())
                .set("value", value.trim())
                .set("note", note)
                .set("is_enabled", is_enabled)
                .set("gmt_modified", System.currentTimeMillis());

        if (row_id > 0) {
            qr.whereEq("row_id", row_id).update();
        } else {
            qr.insert();
        }

        if (WW.whitelist_tag_server.equals(tag)) {
            NoticeUtils.updateCache("whitelist:server");
        }

        return true;
    }

    //批量导入
    public static void impWhitelistOrRep(String tag, WhitelistModel wm) throws SQLException {
        if (TextUtils.isEmpty(tag) == false) {
            wm.tag = tag;
        }

        if (TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.value)) {
            return;
        }

        //不存在替换的概念
        db().table("water_cfg_whitelist")
                .set("tag", wm.tag)
                .set("type", wm.type)
                .set("value", wm.value)
                .set("note", wm.note)
                .set("gmt_modified", System.currentTimeMillis())
                .upsertBy("tag,type,value");
    }

    //删除
    public static boolean delWhitelist(int row_id) throws SQLException {
        return db().table("water_cfg_whitelist")
                .where("row_id = ?", row_id)
                .delete() > 0;
    }

    //批量删除
    public static void delWhitelistByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (act == 9) {
            db().table("water_cfg_whitelist")
                    .whereIn("row_id", list)
                    .delete();
        } else {
            db().table("water_cfg_whitelist")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .set("gmt_modified", System.currentTimeMillis())
                    .whereIn("row_id", list)
                    .update();
        }
    }

    public static WhitelistModel getWhitelist(int row_id) throws SQLException {
        return db().table("water_cfg_whitelist")
                .where("row_id = ?", row_id)
                .selectItem("*", WhitelistModel.class);
    }

    public static List<WhitelistModel> getWhitelistByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("water_cfg_whitelist")
                .whereIn("row_id", list)
                .selectList("*", WhitelistModel.class);
    }

    //加载IP白名单到静态缓存里
    public static void reloadWhitelist() throws Exception {
        CacheUtil.data.clear("water_cfg_whitelist");//waterapi那儿，过一分钟就会自动刷新
    }

    public static boolean isWhitelist(String val) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereIn("tag", Arrays.asList(WhitelistApi.tag_client, WhitelistApi.tag_server))
                .andEq("type", "ip")
                .andEq("value", val)
                .caching(CacheUtil.data).usingCache(60)
                .cacheTag("whitelist")
                .selectExists();
    }

    public static String getServerTokenOne() throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("tag", WW.whitelist_tag_server)
                .andEq("type", WW.whitelist_type_token)
                .andEq("is_enabled", 1)
                .caching(CacheUtil.data)
                .cacheTag("whitelist:server")
                .usingCache(60)
                .selectValue("value", "");

    }
}
