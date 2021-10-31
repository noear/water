package watersetup.models.water_cfg;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.water.WW;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.ConfigUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbDataSource;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import org.noear.weed.mongo.MgContext;
import watersetup.dso.ConfigType;

import javax.sql.DataSource;
import java.util.Properties;

@Getter
public class ConfigModel implements IBinder
{
    public transient int row_id;
    public String tag;
    public String key;
    public int type;
    public String value;
    public String edit_mode;
    public transient int is_enabled;



    @Override
    public void bind(GetHandlerEx s) {
        row_id = s.get("row_id").value(0);
        tag = s.get("tag").value("");
        key = s.get("key").value("");
        type = s.get("type").value(0);
        value = s.get("value").value("");
        edit_mode = s.get("edit_mode").value("");
        is_enabled = s.get("is_enabled").value(0);

        if (value.startsWith(WW.cfg_data_header)) {
            value = Base64Utils.decode(value.substring(8));
        }
    }

    @Override
    public IBinder clone() {
        return new ConfigModel();
    }

	public String type_str(){
	    return ConfigType.getTitle(type);
    }


    public String getString() {
        return value;
    }

    public String getString(String def) {
        return value == null ? def : value;
    }

    public String value_html(){
	    if(value == null){
	        return "";
        }else{
	        return value.replace("\n","<br/>")
                    .replace(" ","&nbsp;");
        }
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

    /**
     * 获取 db:DbContext
     */
    public DbContext getDb() {
        return getDb(false);
    }

    public DbContext getDb(boolean pool) {
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

            Utils.injectProperties(source, prop);
            if(TextUtils.isNotEmpty(url)) {
                source.setJdbcUrl(url);
            }

            return source;
        } else {
            if (TextUtils.isNotEmpty(driverClassName)) {
                Utils.loadClass(driverClassName);
            }

            return new DbDataSource(url, username, password);
        }
    }

    public MgContext getMg() {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        Properties prop = getProp();
        String db = prop.getProperty("db");

        if(TextUtils.isEmpty(db)){
            throw new IllegalArgumentException("Missing db configuration");
        }

        return new MgContext(prop, db);
    }

    public MgContext getMg(String db){
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return new MgContext(getProp(), db);
    }
}