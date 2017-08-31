package waterapi.dao;

import noear.weed.cache.ICacheService;
import noear.weed.cache.LocalCache;

/**
 * Created by yuety on 2017/7/17.
 */
public class CacheUtil {
    public static ICacheService data = new LocalCache("data", 60 * 30);
}
