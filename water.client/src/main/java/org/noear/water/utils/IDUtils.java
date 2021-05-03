package org.noear.water.utils;

import org.noear.water.WaterSetting;

import java.util.UUID;

/**
 * 分布式ID工具
 *
 * @author noear
 * @since 2.0
 * */
public class IDUtils {
    private static RedisX _redis_idx = WaterSetting.redis_cfg().getRd(1);

    public static String guid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 雪花ID
     * */
    public static long snowflakeID(){
        return SnowflakeUtils.genId();
    }

    public static long newID(String group, String key, int cacheTime) {
        return _redis_idx.open1((ru) -> ru.key(group).expire(cacheTime).hashIncr(key, 1l));
    }

    public static long newID(String group, String key) {
        return newID(group, key, 60 * 60 * 24 * 365 * 10);
    }

    public static long newIDOfDate(String group, String key) {
        String group2 = group + "." + Datetime.Now().toString("yyyyMMdd");
        return newID(group2, key, 60 * 60 * 25);
    }

    public static long newIDOfHour(String group, String key) {
        String group2 = group + "." + Datetime.Now().toString("yyyyMMddHH");
        return newID(group2, key, 60 * 60 * 2);
    }
}
