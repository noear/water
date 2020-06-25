package waterraas.dao;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.SecondCache;
import waterraas.Config;

public class CacheUtil {
    public static boolean isUsingCache = true;

    //new LocalCache("rubber",300);//

    private static ICacheServiceEx cache1 = new LocalCache(Config.water_service_name + "_DATA", 300);
    private static ICacheServiceEx cache2 = Config.cfg("rock_cache").getCh(Config.water_service_name + "_DATA", 300);

    public static ICacheServiceEx data = new SecondCache(cache1, cache2).nameSet("cache"); ////// new EmptyCache();

    public static void clearByTag(String tag) {
        data.clear(tag);
    }

    public static <T> void updateByTag(String tag, org.noear.weed.ext.Fun1<T, T> setter) {
        data.update(tag, setter);
    }
}
