package org.noear.water;

import org.noear.water.model.ConfigM;
import org.noear.water.utils.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WaterConfig {
    private static String _water_api_url = null;

    public static String water_api_url() {
        return _water_api_url;
    }

    static {
        String host = System.getProperty(WCX.water_host);
        if (TextUtils.isEmpty(host) == false) {

            if (host.indexOf("://") < 0) {
                host = "http://" + host;
            }

            if (host.endsWith("/")) {
                _water_api_url = host;
            } else {
                _water_api_url = host + "/";
            }
        }

        if (TextUtils.isEmpty(_water_api_url)) {
            throw new RuntimeException("System.getProperty(\"water.host\") is null, please configure!");
        }
    }

    public static final ExecutorService pools = Executors.newCachedThreadPool();

    private static final String lock = "";
    private static ConfigM _redis_cfg;
    private static ConfigM _redis_track_cfg;
    private static ConfigM _cache_cfg;

    public static ConfigM redis_cfg() {
        if (_redis_cfg == null) {
            synchronized (lock) {
                if (_redis_cfg == null) {
                    _redis_cfg = cfg(WCX.water_redis);
                }
            }
        }

        return _redis_cfg;
    }

    public static ConfigM redis_track_cfg() {
        if (_redis_track_cfg == null) {
            synchronized (lock) {
                if (_redis_track_cfg == null) {
                    _redis_track_cfg = cfg(WCX.water_redis_track);
                }

                if (_redis_track_cfg == null || TextUtils.isEmpty(_redis_track_cfg.value)) {
                    _redis_track_cfg = cfg(WCX.water_redis);
                }
            }
        }

        return _redis_track_cfg;
    }

    public static ConfigM cache_cfg() {
        if (_cache_cfg == null) {
            synchronized (lock) {
                if (_cache_cfg == null) {
                    _cache_cfg = cfg(WCX.water_cache);
                }
            }
        }

        return _cache_cfg;
    }


    public static void reload() {
        _redis_cfg = cfg(WCX.water_redis);
        _cache_cfg = cfg(WCX.water_cache);
    }


    private static ConfigM cfg(String key) {
        return WaterClient.Config.get(WCX.water, key);
    }
}
