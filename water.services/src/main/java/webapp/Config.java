package webapp;

import org.noear.solon.XApp;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.ServerUtil;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.memcached.MemCache;
import webapp.dso.db.DbApi;
import webapp.dso.db.DbServiceApi;
import webapp.model.ConfigModel;

import java.util.Properties;

public class Config {
    public static final String water_service_name = "waterapi";
    public static final String water_config_tag = "water";

    public static DbContext water;
    public static DbContext water_msg;
    public static DbContext water_log;

    public static RedisX rd_ids;
    public static RedisX rd_track;

    public static String water_cache_header;
    public static String water_msg_queue;

    /**
     * 尝试初始化
     * */
    private static boolean _inited=false;
    public static void tryInit(int service_port, Properties prop) {
        if (_inited == false) {
            _inited = true;

            WeedConfig.isDebug = false;
            WeedConfig.isUsingValueExpression = false;

            water = new DbContext(prop);

            water_msg = getDbConfig("water_msg");
            water_log = getDbConfig("water_log");

            rd_ids= getRdConfig("water_redis",1);
            rd_track = getRdConfig("water_redis",5);

            water_cache_header = getValConfig("water_cache_header","WATER_CACHE") +"_API";
            water_msg_queue = getValConfig("water_msg_queue");

            try {
                DbServiceApi.addService(water_service_name,
                        ServerUtil.getFullAddress(service_port),
                        "",
                        "",
                        "/run/check/",
                        0);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }


    //================================
    //
    //获取一个数据库配置

    public static ConfigModel getConfig(String key){
        try{
            return DbApi.getConfig(water_config_tag,key);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static DbContext getDbConfig(String key) {
        ConfigModel cfg = getConfig(key);

        if (cfg == null) {
            return null;
        } else {
            return new DbContext(cfg.toProp());
        }
    }

    //获取一个数据库配置
    public static RedisX getRdConfig(String key, int db) {
        ConfigModel cfg = getConfig(key);

        if (cfg == null) {
            return null;
        } else {
            return new RedisX(cfg.toProp(), db);
        }
    }

    //获取一个缓存配置
    public static ICacheServiceEx getChConfig(String key, String keyHeader, int defSeconds) {
        ConfigModel cfg = getConfig(key);

        if (cfg == null) {
            return new org.noear.weed.cache.EmptyCache();
        } else {
            return new MemCache(cfg.toProp(), keyHeader, defSeconds);
        }
    }

    public static String getValConfig(String key) {
        return getValConfig(key,null);
    }

    public static String getValConfig(String key, String def) {
        ConfigModel cfg = getConfig(key);

        if (cfg == null) {
            return def;
        } else {
            return cfg.value;
        }
    }
}
