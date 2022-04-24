package wateradmin.models.water_cfg;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.ConfigUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.*;
import org.noear.weed.mongo.MgContext;
import wateradmin.dso.ConfigType;

import javax.sql.DataSource;
import java.util.*;

@Getter
public class ConfigModel implements IBinder {
    public int row_id;
    public String tag;
    public String key;
    public int type;
    public String value;
    public String edit_mode;
    public int is_enabled;


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


    public boolean disabled() {
        return is_enabled == 0;
    }

    public String type_str() {
        return ConfigType.getTitle(type);
    }


    public String getString() {
        return value;
    }

    public String getString(String def) {
        return value == null ? def : value;
    }

    public String value_html() {
        if (value == null) {
            return "";
        } else {
            return value.replace("\n", "<br/>")
                    .replace(" ", "&nbsp;");
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

    private ConfigM configM;

    public ConfigM toConfigM() {
        if (configM == null) {
            configM = new ConfigM(key, value, 0);
        }

        return configM;
    }
}