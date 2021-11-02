package org.noear.rubber;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import waterraas.utils.SnowflakeUtils;

final class RcConfig {
    public static ICacheServiceEx inner_cache = new LocalCache("rubber", 60 * 60 * 24);

    protected static ICacheServiceEx _data_cache;

    public static ICacheServiceEx data_cache() {
        if (_data_cache == null)
            return RcConfig.inner_cache;
        else
            return _data_cache;
    }


    private static DbContext _water_raas;

    public static DbContext water_raas() {
        if (_water_raas == null) {
            _water_raas = cfg(WW.water_raas).getDb(true);
        }

        if (_water_raas == null) {
            //如果没有 water_raas ，则以 water 库
            _water_raas = cfg(WW.water).getDb(true);
        }

        return _water_raas;
    }

    public static boolean is_debug = false;


    public static long newLogID() {
        return SnowflakeUtils.genId(); //IDUtils.newID("rubber", "log_id", 60 * 60 * 24 * 365) + 10000;
    }

    public static ConfigM cfg(String key) {
        if (key.indexOf("/") < 0) {
            return WaterClient.Config.get(WW.water, key);
        } else {
            return WaterClient.Config.getByTagKey(key);
        }
    }
}
