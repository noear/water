package org.noear.water.montior;

import org.noear.water.utils.Datetime;
import org.noear.water.utils.RedisHashWarp;
import org.noear.water.utils.RedisX;

public class MontiorSynchronizer {

    //记录性能
    public static void track(RedisX.RedisUsing ru, String rdKey, MonitorCounter mc) {
        Datetime now = Datetime.Now();
        String log_time = now.toString("yyyy-MM-dd HH:mm:ss");

        //2.记录性能值
        if (MonitorCounter.type_date.equals(mc.type)) {
            long average = do_track_key_minute(ru, mc.key_minute_bef, mc.key_minute, mc); //生成基于分的平均响应

            //记录当日数据
            do_track_key_date(ru, rdKey, mc, average);
        } else {
            //记录当时数据
            do_track_key_hour(ru, rdKey, mc);
        }

        //记录总数据
        ru.key("monitor_keys")
                .expire(60 * 60 * 24 * 365)
                .hashSet(mc.group, log_time);

    }

    private static long do_track_key_minute(RedisX.RedisUsing ru, String rdkey_bef, String rdkey, MonitorCounter mc) {
        RedisHashWarp hash = ru.key(rdkey_bef).hashGetAll();//改用 getAll，减少一连接请求

        long total_time0 = hash.getLong("total_time");
        long total_num0 = hash.getLong("total_num");

        ru.key(rdkey).expire(60 * 3); //10分钟

        long total_time = ru.hashIncr("total_time", mc.total_time());
        long total_num = ru.hashIncr("total_num", mc.total_num());

        //由上一分钟与当前的进行平均 //更好的反应当前的平均性能
        long average = (total_time + total_time0) / (total_num + total_num0); // *** 这个是不安全，不精准的

        return average;
    }

    private static void do_track_key_hour(RedisX.RedisUsing ru, String rdkey, MonitorCounter mc) {
        RedisHashWarp hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
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

        long slowest = hash.getLong("slowest"); //ru.hashVal("slowest");
        long fastest = hash.getLong("fastest"); //ru.hashVal("fastest");

        long slowest2 = mc.slowest();
        long fastest2 = mc.fastest();

        if (slowest2 > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", slowest2); // *** 这个是不安全，不精准的
        }

        if (fastest2 < fastest || fastest == 0) { //更小，就是更快
            ru.hashSet("fastest", fastest2); // *** 这个是不安全，不精准的
        }
    }

    private static void do_track_key_date(RedisX.RedisUsing ru, String rdkey, MonitorCounter mc, long average) {
        RedisHashWarp hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
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

        long slowest = hash.getLong("slowest"); //ru.hashVal("slowest");
        long fastest = hash.getLong("fastest"); //ru.hashVal("fastest");

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
