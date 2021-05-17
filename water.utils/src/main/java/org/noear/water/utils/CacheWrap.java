package org.noear.water.utils;

//import org.noear.weed.cache.ICacheServiceEx;

/**
 * 缓存工具类，包装缓存供Solon和Weed共用
 *
 * @author noear
 * @since 2.0
 * */
//public  class CacheWrap implements WaterCacheService {
//    public static CacheWrap wrap(ICacheServiceEx cache) {
//        return new CacheWrap(cache);
//    }
//
//    private final ICacheServiceEx real;
//
//    public CacheWrap(ICacheServiceEx cache) {
//        this.real = cache;
//    }
//
//    @Override
//    public void store(String key, Object obj, int seconds) {
//        if(seconds == 0) {
//            real.store(key, obj, getDefalutSeconds());
//        }else {
//            real.store(key, obj, seconds);
//        }
//    }
//
//    @Override
//    public Object get(String key) {
//        return real.get(key);
//    }
//
//    @Override
//    public void remove(String key) {
//        real.remove(key);
//    }
//
//    @Override
//    public int getDefalutSeconds() {
//        return real.getDefalutSeconds();
//    }
//
//    @Override
//    public String getCacheKeyHead() {
//        return real.getCacheKeyHead();
//    }
//}

