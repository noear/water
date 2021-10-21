package org.noear.water.utils;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.SecondCache;
import org.noear.weed.cache.memcached.MemCache;
import org.noear.weed.cache.redis.RedisCache;

import java.util.Properties;

/**
 * @author noear 2021/10/21 created
 */
public final class CacheUtils {
    /**
     * 获取 二级缓存 ICacheServiceEx
     */
    public static ICacheServiceEx getCh2(Properties prop, String keyHeader, int defSeconds) {
        ICacheServiceEx cache1 = new LocalCache(defSeconds);
        ICacheServiceEx cache2 = getCh(prop, keyHeader, defSeconds);

        return new SecondCache(cache1, cache2);
    }

    /**
     * 获取 cache:ICacheServiceEx
     */
    public static ICacheServiceEx getCh(Properties prop, String keyHeader, int defSeconds) {
        if ("redis".equals(prop.getProperty("type"))) {
            return new RedisCache(prop, keyHeader, defSeconds);
        } else {
            return new MemCache(prop, keyHeader, defSeconds);
        }
    }
}
