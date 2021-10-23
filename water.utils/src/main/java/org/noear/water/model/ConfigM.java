package org.noear.water.model;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.esearchx.EsContext;
import org.noear.redisx.RedisClient;
import org.noear.snack.ONode;
import org.noear.snack.core.exts.ClassWrap;
import org.noear.snack.core.exts.FieldWrap;
import org.noear.water.WaterProps;
import org.noear.water.utils.CacheUtils;
import org.noear.water.utils.ConfigUtils;
import org.noear.water.utils.ConvertUtil;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbDataSource;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.SecondCache;
import org.noear.weed.mongo.MgContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigM {
    public final String key;
    public final long lastModified;
    public final String value;

    public ConfigM() {
        this.key = null;
        this.lastModified = 0;
        this.value = null;
    }

    public ConfigM(String key, String value, long lastModified) {
        this.key = key;
        this.value = value;
        this.lastModified = lastModified;
    }

    public String getString() {
        return value;
    }

    public String getString(String def) {
        return value == null ? def : value;
    }

    /**
     * 转为Int
     */
    public int getInt(int def) {
        if (TextUtils.isEmpty(value)) {
            return def;
        } else {
            return Integer.parseInt(value);
        }
    }

    public int getInt() {
        return getInt(0);
    }

    /**
     * 转为Long
     */
    public long getLong(long def) {
        if (TextUtils.isEmpty(value)) {
            return def;
        } else {
            return Long.parseLong(value);
        }
    }

    public long getLong() {
        return getLong(0l);
    }

    /**
     * 转为Properties
     */
    private PropertiesM _prop;

    public PropertiesM getProp() {
        if (_prop == null) {
            _prop = ConfigUtils.global.getProp(value);
        }

        return _prop;
    }

    /**
     * 转为ONode
     */
    private ONode _node;

    public ONode getNode() {
        if (_node == null) {
            _node = ConfigUtils.global.getNode(value);
        }

        return _node;
    }

    public <T> T getObject(Class<T> clz) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        if (value.trim().startsWith("{")) {
            return getNode().toObject(clz);
        }

        return getProp().toObject(clz);
    }

    /**
     * 获取 es:EsearchX
     */
    public EsContext getEs() {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return new EsContext(getProp());
    }

    /**
     * 获取 rd:RedisX
     */
    public RedisClient getRd() {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return new RedisClient(getProp());
    }

    public RedisClient getRd(int db) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return new RedisClient(getProp(), db);
    }

    public RedisClient getRd(int db, int maxTotaol) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return new RedisClient(getProp(), db, maxTotaol);
    }

    public MgContext getMg() {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        PropertiesM prop = getProp();
        String db = prop.getProperty("db");

        if (TextUtils.isEmpty(db)) {
            throw new IllegalArgumentException("Missing db configuration");
        }

        return getMgDo(prop, db);
    }

    public MgContext getMg(String db) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return getMgDo(getProp(), db);
    }

    private static Map<String, MgContext> _mgMap = new HashMap<>();
    private MgContext getMgDo(Properties prop, String db) {
        MgContext mg = _mgMap.get(value);

        if (mg == null) {
            synchronized (value.intern()) {
                mg = _mgMap.get(value);

                if (mg == null) {
                    mg = new MgContext(prop, db);
                    _mgMap.put(value, mg);
                }
            }
        }

        return mg;
    }

    /**
     * 获取 cache:ICacheServiceEx
     */
    public ICacheServiceEx getCh(String keyHeader, int defSeconds) {
        if (TextUtils.isEmpty(value)) {
            return new LocalCache(keyHeader, defSeconds);
        }

        return CacheUtils.getCh(getProp(), keyHeader, defSeconds);
    }

    public ICacheServiceEx getCh() {
        String name = WaterProps.service_name();

        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException("System.getProperty(\"water.service.name\") is null, please configure!");
        }

        return getCh(name, 60 * 5);
    }

    /**
     * 获取 二级缓存 ICacheServiceEx
     */
    public ICacheServiceEx getCh2(String keyHeader, int defSeconds) {
        ICacheServiceEx cache1 = new LocalCache(defSeconds);
        ICacheServiceEx cache2 = getCh(keyHeader, defSeconds);

        return new SecondCache(cache1, cache2);
    }

    public ICacheServiceEx getCh2() {
        String name = WaterProps.service_name();

        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException("System.getProperty(\"water.service.name\") is null, please configure!");
        }

        return getCh2(name, 60 * 5);
    }

    /**
     * 获取 db:DbContext
     */
    private static Map<String, DbContext> _dbMap = new HashMap<>();

    public DbContext getDb() {
        return getDb(false);
    }

    public DbContext getDb(boolean pool) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        DbContext db = _dbMap.get(value);

        if (db == null) {
            synchronized (value.intern()) {
                db = _dbMap.get(value);

                if (db == null) {
                    db = getDbDo(pool);
                    _dbMap.put(value, db);
                }
            }

        }
        return db;
    }

    private DbContext getDbDo(boolean pool) {
        Properties prop = getProp();
        String url = prop.getProperty("url");

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String schema = prop.getProperty("schema");

        return new DbContext(getDs(pool), schema);
    }

    public DataSource getDs(boolean pool) {
        Properties prop = getProp();
        String url = prop.getProperty("url");

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driverClassName = prop.getProperty("driverClassName");

        if (pool) {
            HikariDataSource source = new HikariDataSource();

            for (FieldWrap fw : ClassWrap.get(HikariDataSource.class).fieldAllWraps()) {
                String valStr = prop.getProperty(fw.name());
                if (TextUtils.isNotEmpty(valStr)) {
                    Object val = ConvertUtil.to(fw.type, valStr);
                    fw.setValue(source, val);
                }
            }

            if (TextUtils.isNotEmpty(url)) {
                source.setJdbcUrl(url);
            }

            return source;
        } else {
            if (TextUtils.isNotEmpty(driverClassName)) {
                try {
                    Class.forName(driverClassName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return new DbDataSource(url, username, password);
        }
    }
}
