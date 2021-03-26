package org.noear.water.track;

import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.RedisX;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2021/3/26 created
 */
public class TrackNames {
    private static TrackNames singleton = new TrackNames();

    public static TrackNames singleton() {
        return singleton;
    }

    private RedisX _redisX;

    public void bind(RedisX redisX) {
        _redisX = redisX;
    }

    private Map<String, String> _nameSet = new LinkedHashMap<>();

    public String getNameMd5(String name) {
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
        if (nameMd5.startsWith("{md5}") == false) {
            return nameMd5;
        }

        return getDo(nameMd5);
    }

    public String getDo(String nameMd5) {
        return _redisX.open1(ru -> ru.key(nameMd5).get());
    }

    private void setDo(String nameMd5, String name) {
        _redisX.open0(ru -> {
            ru.key(nameMd5).set(name).expire(60 * 60 * 24 * 365);//1年
        });
    }
}
