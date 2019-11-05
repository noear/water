package webapp.dso;

import waterapi.Config;
import waterapi.utils.Datetime;
import waterapi.utils.RedisX;

public class TrackUtil {

    public static void track(String service, String tag, String name, long timespan) {
        try {
            do_track(service, tag, name, timespan);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void do_track(String service, String tag, String name, long timespan) {
        Datetime now = Datetime.Now();

        //为了性能采用 StringBuilder
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

        //记录key
        Config.rd_track.open((ru) -> {

            long average = do_track_key_minute(ru,key_minute_bef, key_minute, timespan); //生成基于分的平均响应

            //记录当时数据
            do_track_key_hour(ru, key_hour, timespan);

            //记录当日数据
            do_track_key_date(ru, key_date, timespan, average);

            ru.key("monitor_keys")
                    .expire(60 * 60 * 24 * 365)
                    .hashSet(key_idx, log_time);
        });
    }

    private static long do_track_key_minute(RedisX.RedisUsing ru,String rdkey_bef, String rdkey, long timespan) {
        RedisX.RedisHash hash = ru.key(rdkey_bef).hashGetAll();//改用 getAll，减少一连接请求

        long total_time0 =  hash.val("total_time");
        long total_num0  =  hash.val("total_num");

        ru.key(rdkey).expire(60 * 3); //10分钟

        long total_time = ru.hashIncr("total_time", timespan);
        long total_num = ru.hashIncr("total_num", 1);

        //由上一分钟与当前的进行平均 //更好的反应当前的平均性能
        long average = (total_time + total_time0 ) / (total_num + total_num0);

        return average;
    }

    private static void do_track_key_hour(RedisX.RedisUsing ru, String rdkey, long timespan) {
        RedisX.RedisHash hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
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

        ru.hashSet("average", average);

        long slowest = hash.val("slowest"); //ru.hashVal("slowest");
        long fastest = hash.val("fastest"); //ru.hashVal("fastest");

        if (timespan > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", timespan);
        }

        if (timespan < fastest || fastest==0) { //更小，就是更快
            ru.hashSet("fastest", timespan);
        }
    }

    private static void do_track_key_date(RedisX.RedisUsing ru, String rdkey, long timespan, long average) {
        RedisX.RedisHash hash = ru.key(rdkey).hashGetAll();//改用 getAll，减少一连接请求
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

        ru.hashSet("average", average); //每分钟的平均值

        long slowest = hash.val("slowest"); //ru.hashVal("slowest");
        long fastest = hash.val("fastest"); //ru.hashVal("fastest");

        if (timespan > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", timespan);
        }

        if (timespan < fastest || fastest==0) { //更小，就是更快
            ru.hashSet("fastest", timespan);
        }
    }
}
