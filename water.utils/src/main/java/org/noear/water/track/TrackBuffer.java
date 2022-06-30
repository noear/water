package org.noear.water.track;

import org.noear.redisx.RedisClient;

import java.util.Map;

public class TrackBuffer extends TrackEventBuffer {
    private static TrackBuffer singleton = new TrackBuffer();

    public static TrackBuffer singleton() {
        return singleton;
    }

    private TrackBuffer() {
        super();
    }

    private RedisClient _redisX;

    public void bind(RedisClient redisX) {
        _redisX = redisX;
    }

    /**
     * 提交并清空
     */
    @Override
    public void flush(Map<String, TrackEvent> mainSet, Map<String, TrackEvent> serviceSet, Map<String, TrackEvent> fromSet) throws Throwable {
        if (_redisX == null) {
            return;
        }

        synchronized (this) {
            if (_redisX != null) {
                _redisX.open((ru) -> {
                    try {
                        for (Map.Entry<String, TrackEvent> kv : mainSet.entrySet()) {
                            TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
                        }

                        for (Map.Entry<String, TrackEvent> kv : serviceSet.entrySet()) {
                            TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
                        }

                        for (Map.Entry<String, TrackEvent> kv : fromSet.entrySet()) {
                            TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                });
            }

            mainSet.clear();
            serviceSet.clear();
            fromSet.clear();
        }
    }
}
