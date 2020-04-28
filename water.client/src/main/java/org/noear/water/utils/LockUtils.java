package org.noear.water.utils;

import org.noear.water.WaterConfig;

public class LockUtils {

    private static RedisX _redis_uni = WaterConfig.redis_cfg().getRd(2);
    //private static RedisX _redis_time = WaterConfig.redis_cfg().getRd(5);

    //尝试把group_key锁给inMaster
    public static boolean tryLock(String group, String key, int inSeconds, String inMaster) {
        String key2 = group + ".lk." + key;

        return _redis_uni.open1((ru) -> {
            if (ru.key(key2).exists() == false) {
                ru.key(key2).expire(inSeconds).lock(inMaster);
            }

            return inMaster.equals(ru.key(key2).get());
        });
    }

    public static boolean tryLock(String group, String key, int inSeconds) {
        String key2 = group + ".lk." + key;

        return _redis_uni.open1((ru) -> {
            if (ru.key(key2).exists() == false) {
                return ru.key(key2).expire(inSeconds).lock("_");
            }else{
                return false;
            }
        });
    }

    //group_key是否已被锁定
    public static boolean isLocked(String group, String key) {
        String key2 = group + ".lk." + key;

        return _redis_uni.open1((ru) -> {
            return ru.key(key2).exists();
        });
    }

    //解锁group_key
    public static void unLock(String group, String key){
        String key2 = group+".lk." + key;

        _redis_uni.open0((ru) -> {
            ru.key(key2).delete();
        });
    }

    //=======================================================

    //一般用于业务唯一约束检查
    //
    public static boolean isUnique(String group, String key) {
        return isUnique(group, key, 3);
    }

    //@key     关键值
    //@seconds 有效时间（秒数）
    //return   是否在有效时间内唯一
    public static boolean isUnique(String group, String key, int inSeconds) {
        String key2 = group + key;

        long val = _redis_uni.open1((ru) -> {
            if (ru.key(key2).exists()) {
                return 2l;
            } else {
                return ru.key(key2).expire(inSeconds).incr(1l);
            }
        });

        return val == 1;
    }
}
