package org.noear.water.client.model;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.snack.ONode;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.RunUtil;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.memcached.MemCache;

import java.io.StringReader;
import java.util.Properties;

/**
 * 配置模型
 * */
public class ConfigM {
    public String key;
    public String value;
    public long lastModified;

    public ConfigM(){

    }

    public ConfigM(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ConfigM(String key, String value, long lastModified) {
        this.key = key;
        this.value = value;
        this.lastModified = lastModified;
    }

    //获取key
    public String getKey() {
        return key;
    }

    //最后更新时间
    public long getLastModified() {
        return lastModified;
    }


    //转为String
    public String toString() {
        return value;
    }

    /**
     * 转为Int
     */
    public int toInt() {
        return Integer.parseInt(value);
    }

    /**
     * 转为Long
     */
    public long toLong() {
        return Long.parseLong(value);
    }

    /**
     * 转为Properties
     */
    public Properties toProp() {
        Properties tmp = new Properties();
        RunUtil.runActEx(() -> tmp.load(new StringReader(value)));
        return tmp;
    }

    /**
     * 转为ONode
     */
    public ONode toNode() {
        return ONode.load(value);
    }

    /**
     * 获取 rd:RedisX
     * */
    public RedisX getRd(int db) {
        return new RedisX(toProp(), db);
    }

    /**
     * 获取 cache:ICacheServiceEx
     * */
    public ICacheServiceEx getCh(String keyHeader, int defSeconds) {
        return new MemCache(toProp(), keyHeader, defSeconds);
    }

    /**
     * 获取 db:DbContext
     * */
    public DbContext getDb(){
        return getDb(false);
    }

    public DbContext getDb(boolean pool) {
        DbContext db = new DbContext();
        Properties prop = toProp();
        String url = prop.getProperty("url");

        if (pool) {
            HikariDataSource source = new HikariDataSource();

            String schema = prop.getProperty("schema");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String driverClassName = prop.getProperty("driverClassName");

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

            db.dataSourceSet(source);
            db.schemaSet(schema);
        } else {
            db.propSet(toProp());
        }

        if (url.indexOf("mysql:") >= 0) {
            db.fieldFormatSet("`%`").objectFormatSet("`%`");
        }

        return db;
    }
}
