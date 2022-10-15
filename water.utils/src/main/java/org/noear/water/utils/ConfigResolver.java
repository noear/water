package org.noear.water.utils;

import org.noear.redisx.RedisClient;
import org.noear.water.model.ConfigM;
import org.noear.wood.DbContext;
import org.noear.mongox.MgContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author noear 2022/4/24 created
 */
public class ConfigResolver {
    //for redis
    //
    private static Map<String, RedisClient> toRedisMap = new HashMap<>();

    public static RedisClient toRedis(ConfigM cfg, int db) {
        if (TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        String cacheKey = cfg.value + "::" + db;

        RedisClient rd = toRedisMap.get(cacheKey);

        if (rd == null) {
            synchronized (cacheKey.intern()) {
                rd = toRedisMap.get(cacheKey);

                if (rd == null) {
                    rd = new RedisClient(cfg.getProp(), db);
                    toRedisMap.put(cacheKey, rd);
                }
            }
        }

        return rd;
    }

    //for mongo
    //
    private static Map<String, MgContext> toMongoMap = new HashMap<>();

    public static MgContext toMongo(ConfigM cfg, String db) {
        if (TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        String cacheKey = cfg.value + "::" + db;

        MgContext mg = toMongoMap.get(cacheKey);

        if (mg == null) {
            synchronized (cacheKey.intern()) {
                mg = toMongoMap.get(cacheKey);

                if (mg == null) {
                    mg = new MgContext(cfg.getProp(), db);
                    toMongoMap.put(cacheKey, mg);
                }
            }
        }

        return mg;
    }

    //for rdb
    private static Map<String, DbContext> toDbMap = new HashMap<>();

    public static DbContext toDb(ConfigM cfg, boolean pool) {
        if (TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        String cacheKey = cfg.value + "::" + pool;

        DbContext db = toDbMap.get(cacheKey);

        if (db == null) {
            synchronized (cacheKey.intern()) {
                db = toDbMap.get(cacheKey);

                if (db == null) {
                    db = getDbDo(cfg, pool);
                    toDbMap.put(cacheKey, db);
                }
            }

        }
        return db;
    }

    private static DbContext getDbDo(ConfigM cfg, boolean pool) {
        Properties prop = cfg.getProp();
        String url = prop.getProperty("url");

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String schema = prop.getProperty("schema");

        return new DbContext(cfg.getDs(pool), schema);
    }
}
