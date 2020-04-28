package org.noear.rubber;

import org.noear.water.WaterClient;
import org.noear.water.utils.IDUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

final class RcConfig {
    public static ICacheServiceEx inner_cache = new LocalCache("rubber",60*60*24);

    protected static ICacheServiceEx _data_cache;
    public static ICacheServiceEx data_cache(){
        if(_data_cache == null)
            return RcConfig.inner_cache;
        else
            return _data_cache;
    }


    private static DbContext _water_log; //rubber_log_request
    private static DbContext _water;
    public static DbContext water(){
        if(_water == null){
            _water = WaterClient.Config.get("water","water").getDb();
        }

        return _water;
    }

    public static DbContext water_log(){
        if(_water_log == null){
            _water_log = WaterClient.Config.get("water","water_log").getDb();
        }

        return _water_log;
    }


    public static boolean is_debug = false;


    public static long newLogID(){
       return IDUtils.newID("rubber", "log_id", 60 * 60 * 24 * 365) + 10000;
    }
}
