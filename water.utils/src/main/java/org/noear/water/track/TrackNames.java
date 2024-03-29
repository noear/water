package org.noear.water.track;

import org.noear.redisx.RedisClient;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author noear 2021/3/26 created
 */
public class TrackNames {
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    private static final TrackNames singleton = new TrackNames();

    public static TrackNames singleton() {
        return singleton;
    }

    private RedisClient _redisX;

    public void bind(RedisClient redisX) {
        _redisX = redisX;
    }

    private Map<String, String> _nameSet = new LinkedHashMap<>();

    public String getNameMd5(String name) {
        if(TextUtils.isEmpty(name)){
            return "";
        }

        if (name.startsWith("{md5}")) {
            return name;
        }

        String nameMd5 = _nameSet.get(name);
        if (nameMd5 == null) {
            synchronized (name.intern()) {
                nameMd5 = _nameSet.get(name);
                if (nameMd5 == null) {
                    nameMd5 = "{md5}" + EncryptUtils.md5(name);
                    _nameSet.put(name, nameMd5);

                    //推到redis
                    setDo(nameMd5, name);
                }
            }
        }

        return nameMd5;
    }

    public String getName(String nameMd5) {
        if(TextUtils.isEmpty(nameMd5)){
            return "";
        }

        if (nameMd5.startsWith("{md5}") == false) {
            return nameMd5;
        }

        return getDo(nameMd5);
    }

    public String getDo(String nameMd5) {
        return _redisX.openAndGet(ru -> ru.key(nameMd5).get());
    }

    private void setDo(String nameMd5, String name) {
        executor.submit(() -> {
            try {
                _redisX.open(ru -> {
                    ru.key(nameMd5).expire(60 * 60 * 24 * 30).set(name);//1月
                });
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });
    }
}
