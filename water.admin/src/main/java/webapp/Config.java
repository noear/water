package webapp;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.water.client.WaterClient;
import org.noear.water.client.model.ConfigM;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.ServerUtil;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.memcached.MemCache;

import java.util.Properties;

public class Config {
    public static String web_title = "WATER";

    public static final String water_service_name = "wateradmin";
    public static final String water_config_tag = "water";

    public static DbContext water;
    public static DbContext water_msg;
    public static DbContext water_log;

    public static RedisX rd_ids;
    public static RedisX rd_track;

    public static String water_cache_header;
    public static String water_msg_queue;

    //是否使用标答检查器？
    public static boolean is_use_tag_checker() {
        return "1".equals(getValConfig("is_use_tag_checker"));
    }


    /**
     * 尝试初始化
     */
    private static boolean _inited = false;

    public static void tryInit(int service_port) {
        if (_inited == false) {
            _inited = true;

            WeedConfig.isDebug = false;
            WeedConfig.isUsingValueExpression = false;


            water = getDbConfig("water", null);

            water_msg = getDbConfig("water_msg", water);
            water_log = getDbConfig("water_log", water);

            rd_ids = getRdConfig("water_redis", 1);
            rd_track = getRdConfig("water_redis", 5);

            water_cache_header = getValConfig("water_cache_header", "WATER_CACHE") + "_API";
            water_msg_queue = getValConfig("water_msg_queue");

            try {
                WaterClient.Registry.add(water_service_name,
                        ServerUtil.getFullAddress(service_port),
                        "/run/check/",
                        "");
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }


    //================================
    //
    //获取一个数据库配置

    public static ConfigM getConfig(String key) {
        try {
            return WaterClient.Config.get(water_config_tag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static DbContext getDb(Properties prop) {
        if (prop.size() < 4) {
            throw new RuntimeException("Data source configuration error!");
        }

        HikariDataSource source = new HikariDataSource();

        String schema = prop.getProperty("schema");
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driverClassName = prop.getProperty("driverClassName");

        if (TextUtils.isEmpty(url) == false) {
            source.setJdbcUrl(url);
        }

        if (TextUtils.isEmpty(username) == false) {
            source.setUsername(username);
        }

        if (TextUtils.isEmpty(password) == false) {
            source.setPassword(password);
        }

        if (TextUtils.isEmpty(schema) == false) {
            source.setSchema(schema);
        }

        if (TextUtils.isEmpty(driverClassName) == false) {
            source.setDriverClassName(driverClassName);
        }

        return new DbContext(schema, source)
                .fieldFormatSet("`%`")
                .objectFormatSet("`%`");
    }

    public static DbContext getDbConfig(String key, DbContext def) {
        ConfigM cfg = getConfig(key);

        if (cfg == null || cfg.value == null) {
            return def;
        } else {
            return getDb(cfg.toProp());
        }
    }

    //获取一个数据库配置
    public static RedisX getRdConfig(String key, int db) {
        ConfigM cfg = getConfig(key);

        if (cfg == null || cfg.value == null) {
            return null;
        } else {
            return new RedisX(cfg.toProp(), db);
        }
    }

    //获取一个缓存配置
    public static ICacheServiceEx getChConfig(String key, String keyHeader, int defSeconds) {
        ConfigM cfg = getConfig(key);

        if (cfg == null || cfg.value == null) {
            return new LocalCache(keyHeader, defSeconds);
        } else {
            return new MemCache(cfg.toProp(), keyHeader, defSeconds);
        }
    }

    public static String getValConfig(String key) {
        return getValConfig(key, null);
    }

    public static String getValConfig(String key, String def) {
        ConfigM cfg = getConfig(key);

        if (cfg == null || cfg.value == null) {
            return def;
        } else {
            return cfg.value;
        }
    }
}
