package org.noear.water.admin.tools.model;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.noear.snack.ONode;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.RunUtil;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.memcached.MemCache;

import java.io.StringReader;
import java.util.Properties;

/// <summary>
/// 生成:2017/12/27 11:14:04
/// 
/// </summary>
@Getter
public class ConfigModel implements IBinder
{
    public int id;
    public String tag;
    public String key;
    public int type;
    public String value;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        id = s.get("id").value(0);
        tag = s.get("tag").value(null);
        key = s.get("key").value(null);
        type = s.get("type").value(0);
	}
	
	public IBinder clone()
	{
		return new ConfigModel();
	}

	public String type_str(){
	    switch (type){
            case 10:return "数据库";
            case 11:return "Redis";
            case 12:return "MangoDb";
            case 20:return "Memcached";
            case 1001:return "阿里云账号";
            default:return "未知";
        }
    }


    //获取key
    public String getKey() {
        return key;
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