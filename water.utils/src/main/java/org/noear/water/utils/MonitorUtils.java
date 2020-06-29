package org.noear.water.utils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MonitorUtils {
    private static final String lock = "";
    private static Map<String, MonitorCounter> _mainSet = new LinkedHashMap<>();
    private static Map<String, MonitorCounter> _serviceSet = new LinkedHashMap<>();
    private static Map<String, MonitorCounter> _fromSet = new LinkedHashMap<>();

    private static RedisX _redisX;
    public static void bind(RedisX redisX){
        _redisX = redisX;
    }

    static {
        long mills = TimeUnit.SECONDS.toMillis(5);
        QuickTimerTask.scheduleAtFixedRate(MonitorUtils::sync, mills, mills);
    }

    private static MonitorCounter getOrNew(Map<String, MonitorCounter> mSet, String group, String rdkey) {

        MonitorCounter tmp = mSet.get(rdkey);

        if (tmp == null) {
            synchronized (lock) {
                tmp = mSet.get(rdkey);
                if (tmp == null) {
                    tmp = new MonitorCounter(group);
                    mSet.put(rdkey, tmp);
                }
            }
        }

        return tmp;
    }

    //记录性能（service/tag/name，三级 ,from _from,at _node）
    public static void track(String service, String tag, String name, long timespan, String _node, String _from) {
        track0(_mainSet, service, tag, name, timespan);

        if (TextUtils.isEmpty(_node) == false) {
            track0(_serviceSet, "_service", service, _node, timespan);
        }

        if (TextUtils.isEmpty(_from) == false) {
            track0(_fromSet, "_from", service, _from, timespan);
        }
    }

    //记录性能（service/tag/name，三级）
    public static void track(String service, String tag, String name, long timespan) {

        track0(_mainSet, service, tag, name, timespan);
    }

    private static void track0(Map<String, MonitorCounter> mSet, String service, String tag, String name, long timespan) {
        try {
            do_track(mSet, service, tag, name, timespan);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //记录性能
    private static void do_track(Map<String, MonitorCounter> mSet, String service, String tag, String name, long timespan) {
        Datetime now = Datetime.Now();

        //1.提前构建各种key（为了性能采用 StringBuilder）
        StringBuilder sb = new StringBuilder();

        sb.append(service).append("$").append(tag).append("$").append(name);
        String key_group = sb.toString();

        sb.append("$").append(now.toString("yyyyMMdd"));
        String key_date = sb.toString();

        sb.append(now.toString("HH"));
        String key_hour = sb.toString();

        sb.append(now.toString("mm"));
        String key_minute = sb.toString();
        String key_minute_bef = now.addMinute(-1).toString("yyyyMMddHHmm");

        //average, slowest, fastest, total_num, total_time

        //记录当时数据
        do_track_key(mSet, key_group, key_hour, timespan, MonitorCounter.type_hour, key_minute, key_minute_bef);

        //记录当日数据
        do_track_key(mSet, key_group, key_date, timespan, MonitorCounter.type_date, key_minute, key_minute_bef);
    }

    private static void do_track_key(Map<String, MonitorCounter> mSet, String group, String rdkey, long timespan, String type, String key_minute,String key_minute_bef) {
        MonitorCounter ru = getOrNew(mSet, group, rdkey);
        ru.type = type;
        ru.key_minute = key_minute;
        ru.key_minute_bef = key_minute_bef;

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

        long slowest = ru.hashVal("slowest"); //ru.hashVal("slowest");
        long fastest = ru.hashVal("fastest"); //ru.hashVal("fastest");

        if (timespan > slowest) { //更大，就是更慢 //可能会一直没有这个数据，注意后续处理
            ru.hashSet("slowest", timespan); // *** 这个是不安全，不精准的
        }

        if (timespan < fastest || fastest == 0) { //更小，就是更快
            ru.hashSet("fastest", timespan); // *** 这个是不安全，不精准的
        }
    }

    public static void sync() {
        if(_mainSet.size() == 0){
            return;
        }

        if (_redisX != null) {
            _redisX.open0((ru) -> {
                try {
                    sync0(ru);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            });
        }

        System.out.println(_mainSet.size() + "+" + new Date().toString());

        synchronized (lock) {
            _mainSet.clear();
            _serviceSet.clear();
            _fromSet.clear();
        }
    }

    private static void sync0(RedisX.RedisUsing ru) {

        for (Map.Entry<String, MonitorCounter> kv : _mainSet.entrySet()) {
            MontiorSynchronizer.track(ru,kv.getKey(),kv.getValue());
        }

        for (Map.Entry<String, MonitorCounter> kv : _serviceSet.entrySet()) {
            MontiorSynchronizer.track(ru,kv.getKey(),kv.getValue());
        }

        for (Map.Entry<String, MonitorCounter> kv : _fromSet.entrySet()) {
            MontiorSynchronizer.track(ru,kv.getKey(),kv.getValue());
        }
    }
}
