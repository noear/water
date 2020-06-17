package org.noear.water;

import org.noear.water.model.ConfigM;
import org.noear.water.utils.TextUtils;

public class WaterConfig {
    private static String _water_api_url = null;
    public static String water_api_url(){ return _water_api_url; }

    static {
        String host = System.getProperty("water.host");
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


    private static final String lock = "";
    private static ConfigM _redis_cfg;
    private static ConfigM _cache_cfg;

    public static ConfigM redis_cfg() {
        if (_redis_cfg == null) {
            synchronized (lock) {
                if (_redis_cfg == null) {
                    _redis_cfg = WaterClient.Config.get("water", "water_redis");
                }
            }
        }

        return _redis_cfg;
    }

    public static ConfigM cache_cfg() {
        if (_cache_cfg == null) {
            synchronized (lock) {
                if (_cache_cfg == null) {
                    _cache_cfg = WaterClient.Config.get("water", "water_cache");
                }
            }
        }

        return _cache_cfg;
    }


    public static void reload(){
        _redis_cfg = WaterClient.Config.get("water", "water_redis");
        _cache_cfg = WaterClient.Config.get("water", "water_cache");
    }
}
