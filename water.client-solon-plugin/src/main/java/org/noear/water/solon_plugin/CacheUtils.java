package org.noear.water.solon_plugin;

import org.noear.solon.core.CacheService;
import org.noear.weed.cache.ICacheServiceEx;

public class CacheUtils {
    public static CacheWrap wrap(ICacheServiceEx cache) {
        return new CacheWrap(cache);
    }

    public static class CacheWrap implements CacheService, ICacheServiceEx {
        private final ICacheServiceEx real;

        public CacheWrap(ICacheServiceEx cache) {
            this.real = cache;
        }

        @Override
        public void store(String key, Object obj, int seconds) {
            real.store(key, obj, seconds);
        }

        @Override
        public Object get(String key) {
            return real.get(key);
        }

        @Override
        public void remove(String key) {
            real.remove(key);
        }

        @Override
        public int getDefalutSeconds() {
            return real.getDefalutSeconds();
        }

        @Override
        public String getCacheKeyHead() {
            return real.getCacheKeyHead();
        }
    }
}
