package org.noear.water.track;

import org.noear.water.utils.Datetime;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TaskUtils;
import org.noear.water.utils.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class TrackBuffer implements TaskUtils.ITask {
    private static TrackBuffer singleton = new TrackBuffer();

    public static TrackBuffer singleton() {
        return singleton;
    }

    private TrackBuffer() {
        TaskUtils.run(this);
    }

    //
    //
    //

    private final String lock = "";
    private Map<String, TrackEvent> _mainSet = new LinkedHashMap<>();
    private Map<String, TrackEvent> _serviceSet = new LinkedHashMap<>();
    private Map<String, TrackEvent> _fromSet = new LinkedHashMap<>();

    private RedisX _redisX;

    public void bind(RedisX redisX) {
        _redisX = redisX;
    }

    private TrackEvent getOrNew(Map<String, TrackEvent> mSet, String group, String rdkey) {

        TrackEvent tmp = mSet.get(rdkey);

        if (tmp == null) {
            synchronized (lock) {
                tmp = mSet.get(rdkey);
                if (tmp == null) {
                    tmp = new TrackEvent(group);
                    mSet.put(rdkey, tmp);
                }
            }
        }

        return tmp;
    }

    /**
     * 添加记录（记录性能（service/tag/name，三级 ,from _from,at _node））
     */
    public void append(String service, String tag, String name, long timespan, String _node, String _from) {
        appendDo(_mainSet, service, tag, name, timespan);

        if (TextUtils.isEmpty(_node) == false) {
            appendDo(_serviceSet, "_service", service, _node, timespan);
        }

        if (TextUtils.isEmpty(_from) == false) {
            appendDo(_fromSet, "_from", service, _from, timespan);
        }
    }

    /**
     * 添加记录（记录性能（service/tag/name，三级））
     */
    public void append(String service, String tag, String name, long timespan) {
        appendDo(_mainSet, service, tag, name, timespan);
    }

    /**
     * 添加记数
     */
    public void appendCount(String service, String tag, String name, int count) {
        appendDo(_mainSet, service, tag, name, count, 0, 0, 0);
    }

    public void appendCount(String service, String tag, String name, int count, int count1, int count2, int count5) {
        appendDo(_mainSet, service, tag, name, count, count1, count2, count5);
    }

    private void appendDo(Map<String, TrackEvent> mSet, String service, String tag, String name, long timespan) {
        try {
            appendDo0(mSet, service, tag, name, timespan, 1, 1, 1, 1);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void appendDo(Map<String, TrackEvent> mSet, String service, String tag, String name, int count, int count1, int count2, int count5) {
        try {
            appendDo0(mSet, service, tag, name, -1, count, count1, count2, count5);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    //记录性能
    private void appendDo0(Map<String, TrackEvent> mSet, String service, String tag, String name, long timespan, int count, int count1, int count2, int count5) {
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
        do_track_key(mSet, key_group, key_hour, timespan, TrackEvent.type_hour, key_minute, key_minute_bef, count, count1, count2, count5);

        //记录当日数据
        do_track_key(mSet, key_group, key_date, timespan, TrackEvent.type_date, key_minute, key_minute_bef, count, count1, count2, count5);
    }

    private void do_track_key(Map<String, TrackEvent> mSet, String group, String rdkey, long timespan, String type, String key_minute, String key_minute_bef, int count, int count1, int count2, int count5) {
        TrackEvent ru = getOrNew(mSet, group, rdkey);
        ru.type = type;
        ru.key_minute = key_minute;
        ru.key_minute_bef = key_minute_bef;

        if (timespan < 0) {
            ru.hashIncr("total_num", count);

            if (count1 > 0) {
                ru.hashIncr("total_num_slow1", count1);
            }
            if (count2 > 0) {
                ru.hashIncr("total_num_slow2", count2);
            }
            if (count5 > 0) {
                ru.hashIncr("total_num_slow5", count5);
            }

            ru.hashVal("slowest"); //ru.hashVal("slowest");
            ru.hashVal("fastest"); //ru.hashVal("fastest");
        } else {
            ru.hashIncr("total_num", count);

            if (timespan > 1000) {
                ru.hashIncr("total_num_slow1", count1);
            }

            if (timespan > 2000) {
                ru.hashIncr("total_num_slow2", count2);
            }

            if (timespan > 5000) {
                ru.hashIncr("total_num_slow5", count5);
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
    }

    private long interval = 1000;
    private long interval_min = 100;

    /**
     * 获取任务间隔时间
     */
    @Override
    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        if (interval >= interval_min) {
            this.interval = interval;
        }
    }

    @Override
    public void exec() throws Throwable {
        flush();
    }

    /**
     * 提交并清空
     */
    public void flush() {
        if (_redisX == null) {
            return;
        }

        if (_mainSet.size() == 0) {
            return;
        }

        synchronized (lock) {
            if (_redisX != null) {
                _redisX.open0((ru) -> {
                    try {
                        flush0(ru);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                });
            }

            _mainSet.clear();
            _serviceSet.clear();
            _fromSet.clear();
        }
    }

    private void flush0(RedisX.RedisUsing ru) {
        for (Map.Entry<String, TrackEvent> kv : _mainSet.entrySet()) {
            TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
        }

        for (Map.Entry<String, TrackEvent> kv : _serviceSet.entrySet()) {
            TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
        }

        for (Map.Entry<String, TrackEvent> kv : _fromSet.entrySet()) {
            TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
        }
    }
}
