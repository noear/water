package webapp;

import org.noear.water.tools.RedisX;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import webapp.model.ConfigModel;

public class Config {
    public static final String water_service_name = "waterapi";
    public static final String water_config_tag = "water";

    public static DbContext water = getDbConfig("water");
    public static DbContext water_msg = getDbConfig("water_msg");
    public static DbContext water_log = getDbConfig("water_log");

    public static RedisX rd_ids   = getRdConfig("water_redis",1);
    public static RedisX rd_track = getRdConfig("water_redis",5);

    public static boolean is_debug = "1".equals(getUrlConfig("is_debug"));

    public static String water_cache_header = getUrlConfig("water_cache_header","WATER2_CACHE") +"_API";

    public static String water_msg_queue = getUrlConfig("water_msg_queue");




    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;
        WeedConfig.isUsingValueExpression = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //================================
    //
    //获取一个数据库配置
    public static DbContext getDbConfig(String key) {
        ConfigModel cfg = WaterCfg.Config.get(key);

        if (cfg == null) {
            return null;
        } else {
            return new DbContext(cfg.key.replace("_r", ""), cfg.url, cfg.user, cfg.password);
        }
    }

    //获取一个数据库配置
    public static RedisX getRdConfig(String key, int db) {
        WaterCfg.ConfigModel cfg = WaterCfg.Config.get(key);

        if (cfg == null) {
            return null;
        } else {
            return new RedisX(cfg.url, cfg.user,cfg.password, db);
        }
    }

    //获取一个缓存配置
    public static ICacheServiceEx getChConfig(String key, String keyHeader, int defSeconds) {
        WaterCfg.ConfigModel cfg = WaterCfg.Config.get(key);

        if (cfg == null) {
            return new org.noear.weed.cache.EmptyCache();
        } else {
            return new MemCache(keyHeader, defSeconds, cfg.url, cfg.user, cfg.password);
        }
    }

    public static String getUrlConfig(String key) {
        return getUrlConfig(key,null);
    }

    public static String getUrlConfig(String key, String def) {
        WaterCfg.ConfigModel cfg = WaterCfg.Config.get(key);

        if (cfg == null) {
            return def;
        } else {
            return cfg.url;
        }
    }

    private static boolean _inited=false;
    public static void tryInit(int service_port) {
        if (_inited == false) {
            _inited = true;

            try {
                DbSevApi.addService(water_service_name, ServerUtil.getFullAddress(service_port), "", "","/run/check/", 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
