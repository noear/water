package waterapi.models;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.ConfigUtils;
import org.noear.water.utils.TextUtils;
import org.noear.wood.DbContext;
import org.noear.wood.GetHandlerEx;
import org.noear.wood.IBinder;

import java.util.Date;
import java.util.Properties;

public class ConfigModel implements IBinder {
    public String tag;
    public String key;
    public String value;
    public String edit_mode;
    public long gmt_modified;
    public boolean is_editable;
    public boolean is_enabled;

    @Override
    public void bind(GetHandlerEx s) {
        tag = s.get("tag").value("");
        key = s.get("key").value("");
        value = s.get("value").value("");
        edit_mode = s.get("edit_mode").value("");
        is_editable = s.get("is_editable").value(false);
        is_enabled = s.get("is_enabled").intValue(0) > 0;
        gmt_modified = s.get("gmt_modified").value(0L);

        if (value.startsWith(WW.cfg_data_header)) {
            value = Base64Utils.decode(value.substring(8));
        }
    }

    @Override
    public IBinder clone() {
        return new ConfigModel();
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
            _prop = Utils.buildProperties(value);
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


    private ConfigM configM;
    public ConfigM toConfigM() {
        if (configM == null) {
            configM = new ConfigM(key, value, gmt_modified);
        }

        return configM;
    }
}
