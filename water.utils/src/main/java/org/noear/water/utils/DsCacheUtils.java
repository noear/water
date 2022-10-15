package org.noear.water.utils;

import org.noear.wood.DbContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
public class DsCacheUtils {
    static Map<String, DbContext> dbMap = new HashMap<>();

    public static DbContext getDb(String cfg, boolean pool) {
        return getDb(cfg, pool, null);
    }

    public static DbContext getDb(String cfg, boolean pool, DbContext def) {
        if(TextUtils.isEmpty(cfg)){
            return def;
        }

        DbContext db = dbMap.get(cfg);

        if (db == null) {
            Properties prop = PropUtils.build(cfg);
            db = DsUtils.getDb(prop, pool);
            dbMap.putIfAbsent(cfg, db);
        }

        return db;
    }
}
