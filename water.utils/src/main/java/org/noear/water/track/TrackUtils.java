package org.noear.water.track;

import org.noear.redisx.RedisClient;
import org.noear.redisx.RedisSession;
import org.noear.redisx.model.LocalHash;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;

public class TrackUtils {

    //记录性能（service/tag/name，三级 ,from _from,at _node）
    public static void track(RedisClient redisX, String service, String tag, String name, long timespan, String _node, String _from) {
        TrackUtils.track(redisX, service, tag, name, timespan);

        if (TextUtils.isEmpty(_node) == false) {
            TrackUtils.track(redisX, "_service", service, _node, timespan);
        }

        if (TextUtils.isEmpty(_from) == false) {
            TrackUtils.track(redisX, "_from", service, _from, timespan);
        }
    }

    //记录性能（service/tag/name，三级）
    public static void track(RedisClient redisX, String service, String tag, String name, long timespan) {
        try {
            do_track(redisX, service, tag, name, timespan);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //记录性能
    private static void do_track(RedisClient redisX, String service, String tag, String name, long timespan) {
        Datetime now = Datetime.Now();

        //1.提前构建各种key（为了性能采用 StringBuilder）
        StringBuilder sb = new StringBuilder();

        sb.append(service).append("$").append(tag).append("$").append(name);
        String key_idx = sb.toString();

        sb.append("$").append(now.toString("yyyyMMdd"));
        String key_date = sb.toString();

        sb.append(now.toString("HH"));
        String key_hour = sb.toString();

        sb.append(now.toString("mm"));
        String key_minute = sb.toString();
        String key_minute_bef = now.addMinute(-1).toString("yyyyMMddHHmm");

        //average, slowest, fastest, total_num, total_time

        String log_time = now.toString("yyyy-MM-dd HH:mm:ss");

        //2.记录性能值
        redisX.open((ru) -> {

            long average = do_track_key_minute(ru, key_minute_bef, key_minute, timespan); //生成基于分的平均响应

            //记录当时数据
            do_track_key_hour(ru, key_hour, timespan);

            //记录当日数据
            do_track_key_date(ru, key_date, timespan, average);

            //记录总数据
            ru.key("monitor_keys")
                    .expire(60 * 60 * 24 * 365)
                    .hashSet(key_idx, log_time);
        });
    }

    private static long do_track_key_minute(RedisSession ru, String rdkey_bef, String rdkey, long timespan) {
        LocalHash hash = ru.key(rdkey_bef).hashGetAll();//改用 getAll，减少一连接请求

        long total_time0 = hash.getAsLong("total_time");
        long total_num0 = hash.getAsLong("total_num");

        ru.key(rdkey).expire(60 * 3); //10分钟

        long total_time = ru.hashIncr("total_time", timespan);
        long total_num = ru.hashIncr("total_num", 1);

        //由上一分钟与当前的进行平均 //更好的反应当前的平均性能
        long average = (total_time + total_time0) / (total_num + total_num0); // *** 这个是不安全，不精准的

        return average;
    }

    private static void do_track_key_hour(RedisSession ru, String rdkey, long timespan) {
        LocalHash hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
        ru.key(rdkey).expire(60 * 60 * 3);

        long total_time = ru.hashIncr("total_time", timespan);
        long total_num = ru.hashIncr("total_num", 1);

        if (timespan > 1000) {
            ru.hashIncr("total_num_slow1", 1);
        }

        if (timespan > 2000) {
            ru.hashIncr("total_num_slow2", 1);
        }

        if (timespan > 5000) {
            ru.hashIncr("total_num_slow5", 1);
        }

        long average = total_time / total_num;

        ru.hashSet("average", average); // *** 这个是不安全，不精准的

        long slowest = hash.getAsLong("slowest"); //ru.hashVal("slowest");
        long fastest = hash.getAsLong("fastest"); //ru.hashVal("fastest");

        if (timespan > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", timespan); // *** 这个是不安全，不精准的
        }

        if (timespan < fastest || fastest == 0) { //更小，就是更快
            ru.hashSet("fastest", timespan); // *** 这个是不安全，不精准的
        }
    }

    private static void do_track_key_date(RedisSession ru, String rdkey, long timespan, long average) {
        LocalHash hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
        ru.key(rdkey).expire(60 * 60 * 24);

        ru.hashIncr("total_time", timespan); //没有必要了
        ru.hashIncr("total_num", 1);

        if (timespan > 1000) {
            ru.hashIncr("total_num_slow1", 1);
        }

        if (timespan > 2000) {
            ru.hashIncr("total_num_slow2", 1);
        }

        if (timespan > 5000) {
            ru.hashIncr("total_num_slow5", 1);
        }

        ru.hashSet("average", average); //每分钟的平均值 // *** 这个是不安全，不精准的

        long slowest = hash.getAsLong("slowest"); //ru.hashVal("slowest");
        long fastest = hash.getAsLong("fastest"); //ru.hashVal("fastest");

        if (timespan > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", timespan); // *** 这个是不安全，不精准的
        }

        if (timespan < fastest || fastest == 0) { //更小，就是更快
            ru.hashSet("fastest", timespan); // *** 这个是不安全，不精准的
        }
    }

    //
    // trackAll
    //

    //记录性能
    public static void trackAll(RedisSession ru, String rdKey, TrackEvent mc) {
        Datetime now = Datetime.Now();
        String log_time = now.toString("yyyy-MM-dd HH:mm:ss");

        //2.记录性能值
        if (TrackEvent.type_date.equals(mc.type)) {
            long average = trackAll_key_minute(ru, mc.key_minute_bef, mc.key_minute, mc); //生成基于分的平均响应

            //记录当日数据
            trackAll_key_date(ru, rdKey, mc, average);
        } else {
            //记录当时数据
            trackAll_key_hour(ru, rdKey, mc);
        }

        //记录总数据
        ru.key("monitor_keys")
                .expire(60 * 60 * 24 * 365)
                .hashSet(mc.group, log_time);

    }

    private static long trackAll_key_minute(RedisSession ru, String rdkey_bef, String rdkey, TrackEvent mc) {
        LocalHash hash = ru.key(rdkey_bef).hashGetAll();//改用 getAll，减少一连接请求

        long total_time0 = hash.getAsLong("total_time");
        long total_num0 = hash.getAsLong("total_num");

        ru.key(rdkey).expire(60 * 3); //10分钟

        long total_time = ru.hashIncr("total_time", mc.total_time());
        long total_num = ru.hashIncr("total_num", mc.total_num());

        //由上一分钟与当前的进行平均 //更好的反应当前的平均性能
        long average = (total_time + total_time0) / (total_num + total_num0); // *** 这个是不安全，不精准的

        return average;
    }

    private static void trackAll_key_hour(RedisSession ru, String rdkey, TrackEvent mc) {
        LocalHash hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
        ru.key(rdkey).expire(60 * 60 * 3);

        long total_time = ru.hashIncr("total_time", mc.total_time());
        long total_num = ru.hashIncr("total_num", mc.total_num());

        long total_num_slow1 = mc.total_num_slow1();
        long total_num_slow2 = mc.total_num_slow2();
        long total_num_slow5 = mc.total_num_slow5();

        if (total_num_slow1 > 0) {
            ru.hashIncr("total_num_slow1", total_num_slow1);
        }

        if (total_num_slow2 > 0) {
            ru.hashIncr("total_num_slow2", total_num_slow2);
        }

        if (total_num_slow5 > 0) {
            ru.hashIncr("total_num_slow5", total_num_slow5);
        }

        long average = total_time / total_num;

        ru.hashSet("average", average); // *** 这个是不安全，不精准的

        long slowest = hash.getAsLong("slowest"); //ru.hashVal("slowest");
        long fastest = hash.getAsLong("fastest"); //ru.hashVal("fastest");

        long slowest2 = mc.slowest();
        long fastest2 = mc.fastest();

        if (slowest2 > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", slowest2); // *** 这个是不安全，不精准的
        }

        if (fastest2 < fastest || fastest == 0) { //更小，就是更快
            ru.hashSet("fastest", fastest2); // *** 这个是不安全，不精准的
        }
    }

    private static void trackAll_key_date(RedisSession ru, String rdkey, TrackEvent mc, long average) {
        LocalHash hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
        ru.key(rdkey).expire(60 * 60 * 24);

        ru.hashIncr("total_time", mc.total_time()); //没有必要了
        ru.hashIncr("total_num", mc.total_num());

        long total_num_slow1 = mc.total_num_slow1();
        long total_num_slow2 = mc.total_num_slow2();
        long total_num_slow5 = mc.total_num_slow5();

        if (total_num_slow1 > 0) {
            ru.hashIncr("total_num_slow1", total_num_slow1);
        }

        if (total_num_slow2 > 0) {
            ru.hashIncr("total_num_slow2", total_num_slow2);
        }

        if (total_num_slow5 > 0) {
            ru.hashIncr("total_num_slow5", total_num_slow5);
        }

        ru.hashSet("average", average); //每分钟的平均值 // *** 这个是不安全，不精准的

        long slowest = hash.getAsLong("slowest"); //ru.hashVal("slowest");
        long fastest = hash.getAsLong("fastest"); //ru.hashVal("fastest");

        long slowest2 = mc.slowest();
        long fastest2 = mc.fastest();

        if (slowest2 > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", slowest2); // *** 这个是不安全，不精准的
        }

        if (fastest2 < fastest || fastest == 0) { //更小，就是更快
            ru.hashSet("fastest", fastest2); // *** 这个是不安全，不精准的
        }
    }
}
