package org.noear.water.track;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class TrackEvent implements Serializable {
    public static final String type_date = "date";
    public static final String type_hour = "hour";

    private Map<String,LongAdder> _hash;

    public String type = type_date;
    public String group;
    public String key_minute;
    public String key_minute_bef;

    public TrackEvent(){
        _hash = new HashMap<>();
    }

    public TrackEvent(String group){
        this();
        this.group = group;
    }

    private LongAdder getOrNew(String key){
        LongAdder tmp = _hash.get(key);
        if(tmp == null){
            synchronized (key.intern()){
                tmp = _hash.get(key);
                if(tmp == null){
                    tmp = new LongAdder();
                    _hash.put(key,tmp);
                }
            }
        }
        return tmp;
    }

    public void hashIncr(String key, long val){
        LongAdder tmp = getOrNew(key);
        tmp.add(val);
    }

    public void hashSet(String key, long val){
        LongAdder tmp = getOrNew(key);
        tmp.reset();
        tmp.add(val);
    }

    public long hashVal(String key){
        LongAdder tmp = getOrNew(key);
        return tmp.longValue();
    }

    public long total_time(){
        return hashVal("total_time");
    }

    public long total_num(){
        return hashVal("total_num");
    }

    public long total_num_slow1(){
        return hashVal("total_num_slow1");
    }

    public long total_num_slow2(){
        return hashVal("total_num_slow2");
    }

    public long total_num_slow5(){
        return hashVal("total_num_slow5");
    }

    public long slowest(){
        return hashVal("slowest");
    }

    public long fastest(){
        return hashVal("fastest");
    }
}
