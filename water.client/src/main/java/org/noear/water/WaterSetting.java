package org.noear.water;

import org.noear.redisx.RedisClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.HostUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Water 内部设置
 *
 * @author noear
 * @since 2.0
 * */
public class WaterSetting {
    public static final Map<String, DbContext> libOfDb = new ConcurrentHashMap();
    public static final Map<String, RedisClient> libOfRd = new ConcurrentHashMap();
    public static final Map<String, ICacheServiceEx> libOfCache = new ConcurrentHashMap();

    private static boolean _water_logger_gzip = true;
    public static boolean water_logger_gzip(){
        return _water_logger_gzip;
    }
    public static void water_logger_gzip(boolean gzip){
        _water_logger_gzip = gzip;
    }


    private static String _water_acl_token;
    public static String water_acl_token(){
        return _water_acl_token;
    }
    public static void water_acl_token(String token){
        _water_acl_token = token;
    }


    private static String _water_logger_def = null;
    public static String water_logger_def(){
        return _water_logger_def;
    }

    private static String _water_api_url = null;
    protected static String water_api_url(){
        return _water_api_url;
    }

    //trace_id_supplier
    private static Supplier<String> _water_trace_id_supplier = ()->"";
    public static Supplier<String> water_trace_id_supplier(){
        return _water_trace_id_supplier;
    }
    public static void water_trace_id_supplier(Supplier<String> supplier){
        if(supplier != null){
            _water_trace_id_supplier = supplier;
        }
    }


    static {
        //默认日志
        _water_logger_def = System.getProperty(WW.water_logger);

        //访问令牌
        _water_acl_token = System.getProperty(WW.water_toekn);
        if (TextUtils.isEmpty(_water_acl_token)) {
            _water_acl_token = System.getProperty("solon.cloud.water.token");
        }

        //接口地址
        _water_api_url = System.getProperty(WW.water_host);
        if (TextUtils.isEmpty(_water_api_url)) {
            _water_api_url = System.getProperty("solon.cloud.water.server");
        }

        if (TextUtils.isNotEmpty(_water_api_url)) {
            _water_api_url = HostUtils.adjust(_water_api_url);
        }

        if (TextUtils.isEmpty(_water_api_url)) {
            throw new RuntimeException("System.getProperty(\"water.host\") is null, please configure!");
        }
    }

    public static final ExecutorService pools = Executors.newCachedThreadPool();
    public static final ICacheServiceEx cacheLocal = new LocalCache();

    private static final String lock = "";
    private static ConfigM _redis_cfg;
    private static ConfigM _redis_track_cfg;
    private static ConfigM _cache_cfg;

    public static ConfigM redis_cfg() {
        if (_redis_cfg == null) {
            synchronized (lock) {
                if (_redis_cfg == null) {
                    _redis_cfg = cfg(WW.water_redis);
                }
            }
        }

        return _redis_cfg;
    }

    public static ConfigM redis_track_cfg() {
        if (_redis_track_cfg == null) {
            synchronized (lock) {
                if (_redis_track_cfg == null) {
                    _redis_track_cfg = cfg(WW.water_redis_track);
                }

                if (_redis_track_cfg == null || TextUtils.isEmpty(_redis_track_cfg.value)) {
                    _redis_track_cfg = cfg(WW.water_redis);
                }
            }
        }

        return _redis_track_cfg;
    }

    public static ConfigM cache_cfg() {
        if (_cache_cfg == null) {
            synchronized (lock) {
                if (_cache_cfg == null) {
                    _cache_cfg = cfg(WW.water_cache);
                }
            }
        }

        return _cache_cfg;
    }


    public static void reload() {
        _redis_cfg = cfg(WW.water_redis);
        _cache_cfg = cfg(WW.water_cache);
    }


    private static ConfigM cfg(String key) {
        return WaterClient.Config.get(WW.water, key);
    }
}
