package webapp.dso;

import org.noear.weed.cache.ICacheServiceEx;
import waterapi.Config;

/**
 * Created by yuety on 2017/7/17.
 */
public class CacheUtil {
    public static boolean isUsingCache = true;
    //public static ICacheService data = new LocalCache("data", 60 * 10);
    public static ICacheServiceEx data = Config.getChConfig("water_cache", Config.water_cache_header, 60 * 10);

}