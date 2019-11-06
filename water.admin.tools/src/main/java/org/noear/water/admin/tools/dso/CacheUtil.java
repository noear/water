package org.noear.water.admin.tools.dso;

import org.noear.water.admin.tools.Config;
import org.noear.weed.cache.ICacheServiceEx;

/**
 * Created by yuety on 2017/7/3.
 */
public class CacheUtil {
   public static boolean isUsingCache = true;


   public static ICacheServiceEx data = Config.getChConfig("water_cache", Config.water_cache_header, 60 * 5);

}
