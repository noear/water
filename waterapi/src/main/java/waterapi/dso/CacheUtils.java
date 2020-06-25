package waterapi.dso;

import org.noear.weed.cache.ICacheServiceEx;
import waterapi.Config;

public class CacheUtils {
    //public static ICacheService data = new LocalCache("data", 60 * 10);
    public static ICacheServiceEx data = Config
            .cfg("water_cache")
            .getCh(Config.water_cache_header, 60 * 10);

}