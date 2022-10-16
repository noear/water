package waterapi.dso;

import org.noear.wood.cache.ICacheServiceEx;
import org.noear.wood.cache.LocalCache;
import org.noear.wood.cache.SecondCache;
import waterapi.Config;

public class CacheUtils {
    private static ICacheServiceEx c1 = new LocalCache("data", 60 * 10);
    private static ICacheServiceEx c2 = Config.cfg("water_cache").getCh("WATERAPI", 60 * 10);

    public static ICacheServiceEx data = c1;//new SecondCache(c1, c2);
}