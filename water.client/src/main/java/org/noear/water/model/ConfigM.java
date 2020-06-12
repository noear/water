package org.noear.water.model;


import com.zaxxer.hikari.HikariDataSource;
import org.noear.snack.ONode;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.RunUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.memcached.MemCache;

import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 转为Properties
     */
    private PropertiesM _prop;

    public PropertiesM getProp() {
        if (_prop == null) {
            _prop = getProp(value);
        }

        return _prop;
    }

    public static PropertiesM getProp(String text){
        PropertiesM properties = new PropertiesM();
        RunUtils.runActEx(() -> properties.load(new StringReader(text)));
        return properties;
    }

    /**
     * 转为ONode
     */
    private ONode _node;

    public ONode getNode() {
        if (_node == null) {
            _node = ONode.load(value);
        }

        return _node;
    }

    public <T> T getObject(Class<T> clz){
        if(TextUtils.isEmpty(value)){
            return  null;
        }

        if(value.trim().startsWith("{")){
            return getNode().toObject(clz);
        }

        return getProp().toObject(clz);
    }


    /**
     * 获取 rd:RedisX
     */
    public RedisX getRd(){
        if(TextUtils.isEmpty(value)){
            return null;
        }

        return new RedisX(getProp());
    }
    
    public RedisX getRd(int db) {
        if(TextUtils.isEmpty(value)){
            return null;
        }

        return new RedisX(getProp(), db);
    }

    public RedisX getRd(int db, int maxTotaol) {
        if(TextUtils.isEmpty(value)){
            return null;
        }

        return new RedisX(getProp(), db, maxTotaol);
    }

    /**
     * 获取 cache:ICacheServiceEx
     */
    public ICacheServiceEx getCh(String keyHeader, int defSeconds) {
        if (TextUtils.isEmpty(value)) {
            return new LocalCache(keyHeader, defSeconds);
        }

        return new MemCache(getProp(), keyHeader, defSeconds);
    }

    public ICacheServiceEx getCh() {
        String name = System.getProperty("water.service.name");

        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException("System.getProperty(\"water.service.name\") is null, please configure!");
        }

        return getCh(name, 60 * 5);
    }

    /**
     * 获取 db:DbContext
     */
    public DbContext getDb() {
        return getDb(false);
    }

    public DbContext getDb(boolean pool) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return getDbDo(pool);
    }

    private DbContext getDbDo(boolean pool) {
        Properties prop = getProp();
        String url = prop.getProperty("url");

        if(TextUtils.isEmpty(url)){
            return null;
        }


        DbContext db = new DbContext();

        if (pool) {

            HikariDataSource source = new HikariDataSource();
            source.setDataSourceProperties(prop);

            String schema = prop.getProperty("schema");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String driverClassName = prop.getProperty("driverClassName");

            String connectionTimeout = prop.getProperty("connectionTimeout");
            String idleTimeout = prop.getProperty("idleTimeout");
            String maxLifetime = prop.getProperty("maxLifetime");
            String maximumPoolSize = prop.getProperty("maximumPoolSize");

            if (TextUtils.isEmpty(url) == false) {
                source.setJdbcUrl(url);
            }

            if (TextUtils.isEmpty(username) == false) {
                source.setUsername(username);
            }

            if (TextUtils.isEmpty(password) == false) {
                source.setPassword(password);
            }

            if (TextUtils.isEmpty(schema) == false) {
                source.setSchema(schema);
            }

            if (TextUtils.isEmpty(driverClassName) == false) {
                source.setDriverClassName(driverClassName);
            }

            if(TextUtils.isEmpty(connectionTimeout) == false){
                source.setConnectionTimeout(Long.parseLong(connectionTimeout));
            }

            if(TextUtils.isEmpty(idleTimeout) == false){
                source.setIdleTimeout(Long.parseLong(idleTimeout));
            }

            if(TextUtils.isEmpty(maxLifetime) == false){
                source.setMaxLifetime(Long.parseLong(maxLifetime));
            }

            if(TextUtils.isEmpty(maximumPoolSize) == false){
                source.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
            }

            db.dataSourceSet(source);
            db.schemaSet(schema);
        } else {
            db.propSet(getProp());
        }

        return db;
    }
}
