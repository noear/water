package waterapi.dso;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.SecondCache;
import waterapi.Config;

public class CacheUtils {
    private static ICacheServiceEx c1 = new LocalCache("data", 60 * 10);
    private static ICacheServiceEx c2 = Config.cfg("water_cache").getCh(Config.water_cache_header, 60 * 10);

    public static ICacheServiceEx data = new SecondCache(c1, c2);
}