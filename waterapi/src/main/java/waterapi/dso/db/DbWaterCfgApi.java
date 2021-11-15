package waterapi.dso.db;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.model.message.BrokerVo;
import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.models.ConfigModel;
import waterapi.models.LoggerModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
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
        return getConfig(tag,key).toConfigM();
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
                .set("update_fulltime",new Date())
                .where("tag=? AND `key`=?", tag, key)
                .update();

        CacheUtils.data.clear("config_" + tag);
    }

    public static void addConfig(String tag, String key, String value) throws SQLException {
        db().table("water_cfg_properties")
                .set("tag", tag)
                .set("key", key)
                .set("value", value)
                .set("is_editable",true)
                .set("update_fulltime",new Date())
                .insert();
    }

    public static ConfigModel getConfigNoCache(String tag, String key) throws SQLException {
        return db().table("water_cfg_properties")
                .where("tag=? AND `key`=?", tag, key)
                .andEq("is_enabled",1)
                .select("*")
                .getItem(new ConfigModel());
    }


    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles() throws SQLException {
        return Config.water.table("water_cfg_whitelist")
                .whereEq("type","mobile")
                .andEq("tag","_alarm")
                .andEq("is_enabled",1)
                .andNeq("value","")
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

    //获取IP白名单
    private static List<String> _whitelist = null;
    private static boolean _whitelist_ignore_client = false ;

    private static synchronized List<String> getWhitelist() throws SQLException {
        if (_whitelist == null) {
            loadWhitelist();
        }

        return _whitelist;
    }

    public static boolean whitelistIgnoreClient(){
        return _whitelist_ignore_client;
    }

    //加载IP白名单到静态缓存里
    public static void loadWhitelist() throws SQLException {
        _whitelist = db().table("water_cfg_whitelist")
                .whereEq("type", "ip")
                .andEq("tag", "server")
                .andEq("is_enabled", 1)
                .select("value")
                .caching(CacheUtils.data).usingCache(60)
                .getArray("value");

        String tmp = getConfig("water", "whitelist_ignore_client", 60).value;

        _whitelist_ignore_client = "1".equals(tmp);
    }

    //检查是否为IP白名单
    public static boolean isWhitelist(String ip) throws SQLException {
        return getWhitelist().contains(ip);
    }

    public static boolean isWhitelist(String tags, String type, String value) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("type", type)
                .andEq("value", value)
                .andEq("is_enabled",1)
                .build((tb)->{
                    if(tags.length()  > 0){
                        if(tags.indexOf(",") < 0){
                            tb.andEq("tag", tags);
                        }else{
                            tb.andIn("tag", Arrays.asList(tags.split(",")));
                        }

                    }
                })
                .caching(CacheUtils.data).usingCache(60)
                .selectExists();

    }


    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger").where("logger=?", logger).limit(1)
                    .select("*")
                    .caching(CacheUtils.data).usingCache(60)
                    .getItem(LoggerModel.class);
        }catch (Exception ex){
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
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static List<BrokerVo> getBrokerList() throws Exception {
        return db().table("water_cfg_broker").whereEq("is_enabled", 1)
                .caching(CacheUtils.data)
                .usingCache(10)
                .selectList("*", BrokerVo.class);
    }
}
