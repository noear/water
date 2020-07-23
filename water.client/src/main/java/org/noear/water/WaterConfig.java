package org.noear.water;

import org.noear.water.dso.WaterUpstream;
import org.noear.water.dso.WaterUpstreamImp;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WaterConfig {
    public static final Map<String, DbContext> libOfDb = new ConcurrentHashMap();
    public static final Map<String, RedisX> libOfRd = new ConcurrentHashMap();
    public static final Map<String, ICacheServiceEx> libOfCache = new ConcurrentHashMap();

    public static boolean enableGzipLog = false;

    private static String _water_logger_def = null;
    public static String water_logger_def(){
        return _water_logger_def;
    }

    private static String _water_api_url = null;
    public static String water_api_url(){
        return _water_api_url;
    }

    //不可修改
    private static WaterUpstream _water_cfg_upstream = new WaterUpstreamImp();
    public static WaterUpstream water_cfg_upstream(){return _water_cfg_upstream;}

    //可以修改
    private static WaterUpstream _water_sev_upstream = new WaterUpstreamImp();
    public static WaterUpstream water_sev_upstream(){return _water_sev_upstream;}
    public static void water_sev_upstream(WaterUpstream upstream) {
        //设置默认代理
        upstream.setAgentDef(water_api_url());
        _water_sev_upstream = upstream;
    }

    static {
        _water_logger_def = System.getProperty(WW.water_logger);

        String host = System.getProperty(WW.water_host);

        if (TextUtils.isEmpty(host) == false) {

            if (host.indexOf("://") < 0) {
                host = "http://" + host;
            }

            if (host.endsWith("/")) {
                _water_api_url = host.substring(0, host.length() - 2);
            } else {
                _water_api_url = host;
            }
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
