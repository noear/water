package waterapi.dso.db;

import org.noear.solon.Utils;
import org.noear.water.model.ConfigM;
import waterapi.models.I18nModel;
import org.noear.water.model.KeyM;
import org.noear.water.protocol.model.message.BrokerVo;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.models.ConfigModel;
import waterapi.models.LoggerModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置服务接口
 * */
public class DbWaterCfgApi {
    private static DbContext db() {
        return Config.water;
    }


    public static List<ConfigModel> getConfigByTag(String tag) throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("tag", tag)
                .andEq("is_enabled", 1)
                .caching(CacheUtils.data)
                .cacheTag("config_" + tag)
                .usingCache(2) //变更通知会延时3秒发,多并发时稍当一下
                .selectList("*", ConfigModel.class);
    }

    public static ConfigM getConfigM(String tag, String key) {
        return getConfig(tag, key).toConfigM();
    }

    public static ConfigModel getConfig(String tag, String key) {
        return getConfig(tag, key, 0);
    }

    public static ConfigModel getConfig(String tag, String key, int cachedSeconds) {
        try {
            return db().table("water_cfg_properties")
                    .where("tag=? AND `key`=?", tag, key)
                    .andEq("is_enabled", 1)
                    .caching(CacheUtils.data)
                    .build(tb -> {
                        if (cachedSeconds > 0) {
                            tb.usingCache(cachedSeconds);
                        }
                    })
                    .select("*")
                    .getItem(new ConfigModel());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void setConfig(String tag, String key, String value) throws SQLException {
        db().table("water_cfg_properties")
                .set("tag", tag)
                .set("key", key)
                .set("value", value)
                .set("gmt_modified", System.currentTimeMillis())
                .where("tag=? AND `key`=?", tag, key)
                .update();

        CacheUtils.data.clear("config_" + tag);
    }

    public static void addConfig(String tag, String key, String value) throws SQLException {
        db().table("water_cfg_properties")
                .set("tag", tag)
                .set("key", key)
                .set("value", value)
                .set("is_editable", true)
                .set("gmt_modified", System.currentTimeMillis())
                .insert();
    }

    public static ConfigModel getConfigNoCache(String tag, String key) throws SQLException {
        return db().table("water_cfg_properties")
                .where("tag=? AND `key`=?", tag, key)
                .andEq("is_enabled", 1)
                .select("*")
                .getItem(new ConfigModel());
    }


    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles() throws SQLException {
        return Config.water.table("water_cfg_whitelist")
                .whereEq("type", "mobile")
                .andEq("tag", "_alarm")
                .andEq("is_enabled", 1)
                .andNeq("value", "")
                .select("value ")
                .caching(CacheUtils.data)
                .getArray(0);
    }

    public static List<String> getAlarmMobiles(String tag) throws SQLException {
        return Config.water.table("water_cfg_whitelist")
                .whereEq("type", "mobile")
                .andEq("tag", tag)
                .andEq("is_enabled", 1)
                .andNeq("value", "")
                .select("value ")
                .caching(CacheUtils.data)
                .getArray(0);
    }


    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger").where("logger=?", logger).limit(1)
                    .select("*")
                    .caching(CacheUtils.data).usingCache(60)
                    .getItem(LoggerModel.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BrokerVo getBroker(String broker) {
        try {
            return db().table("water_cfg_broker")
                    .where("broker = ?", broker)
                    .limit(1)
                    .select("*")
                    .getItem(BrokerVo.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<BrokerVo> getBrokerList() throws Exception {
        return db().table("water_cfg_broker").whereEq("is_enabled", 1)
                .caching(CacheUtils.data)
                .usingCache(10)
                .selectList("*", BrokerVo.class);
    }

    public static KeyM getKey(String access_key) throws SQLException {
        if (Utils.isEmpty(access_key)) {
            return new KeyM();
        }

        return db().table("water_cfg_key")
                .where("access_key = ?", access_key)
                .caching(CacheUtils.data)
                .usingCache(2) //变更通知会延时3秒发,多并发时稍当一下
                .selectItem("*", KeyM.class);
    }

    public static KeyM getKeyById(int key_id) throws SQLException {
        if (key_id == 0) {
            return new KeyM();
        }

        return db().table("water_cfg_key")
                .where("key_id = ?", key_id)
                .caching(CacheUtils.data)
                .usingCache(2) //变更通知会延时3秒发,多并发时稍当一下
                .selectItem("*", KeyM.class);
    }

    public static List<I18nModel> getI18nListByTag(String tag, String bundle, String lang) throws SQLException {
        if (TextUtils.isEmpty(bundle) || TextUtils.isEmpty(tag)) {
            return new ArrayList<>();
        }

        if (lang == null) {
            lang = "";
        }

        if ("default".equals(lang)) {
            lang = "";
        }

        return db().table("water_cfg_i18n")
                .whereEq("tag", tag)
                .andEq("bundle", bundle)
                .andEq("lang", lang)
                .orderBy("name ASC")
                .caching(CacheUtils.data)
                .usingCache(2) //变更通知会延时3秒发,多并发时稍当一下
                .selectList("name,value", I18nModel.class);
    }
}
