package waterapp.models;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.snack.ONode;
import org.noear.solon.XUtil;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.memcached.MemCache;

import java.util.Date;
import java.util.Properties;

public class ConfigModel implements IBinder {
    public String tag;
    public String key;
    public String value;
    public String edit_mode;
    public Date update_fulltime;
    public boolean is_editable;

    @Override
    public void bind(GetHandlerEx s) {
        tag = s.get("tag").value("");
        key = s.get("key").value("");
        value = s.get("value").value("");
        edit_mode = s.get("edit_mode").value("");
        is_editable = s.get("is_editable").value(false);
    }

    @Override
    public IBinder clone() {
        return new ConfigModel();
    }

    public ConfigM toConfigM(){
        return new ConfigM(key,value,0);
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
    private Properties _prop;

    public Properties getProp() {
        if (_prop == null) {
            _prop = XUtil.getProperties(value);
        }

        return _prop;
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


    /**
     * 获取 rd:RedisX
     */
    public RedisX getRd(int db) {
        return new RedisX(getProp(), db);
    }

    public RedisX getRd(int db, int maxTotaol) {
        return new RedisX(getProp(), db, maxTotaol);
    }

    /**
     * 获取 cache:ICacheServiceEx
     */
    public ICacheServiceEx getCh(String keyHeader, int defSeconds) {
        if(TextUtils.isEmpty(value)){
            return new LocalCache(keyHeader,defSeconds);
        }else {
            return new MemCache(getProp(), keyHeader, defSeconds);
        }
    }

    /**
     * 获取 db:DbContext
     */
    public DbContext getDb() {
        return getDb(false);
    }

    public DbContext getDb(boolean pool) {
        DbContext db = new DbContext();
        Properties prop = getProp();
        String url = prop.getProperty("url");

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
