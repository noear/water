package watersetup.models.water_cfg;

import lombok.Getter;
import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.ConfigUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;
import watersetup.dso.ConfigType;

import java.util.Date;
import java.util.Properties;

@Table("water_cfg_properties")
@Getter
public class ConfigModel  implements IBinder{
    /**  */
    @PrimaryKey
    public Integer row_id;
    /** 分组标签 */
    public String tag;
    /** 属性key */
    public String key;
    /** 类型：0:未知，1:数据库；2:Redis；3:MangoDb；4:Memcached */
    public Integer type;
    /** 属性值 */
    public String value;
    /**  */
    public String edit_mode;
    /** 是否可编辑 */
    public Boolean is_editable;
    /** 是否启用 */
    public Integer is_enabled;
    /** 更新时间 */
    public Date update_fulltime;


    @Override
    public void bind(GetHandlerEx s) {
        row_id = s.get("row_id").value(0);
        tag = s.get("tag").value("");
        key = s.get("key").value("");
        type = s.get("type").value(0);
        value = s.get("value").value("");
        edit_mode = s.get("edit_mode").value("");
        is_editable = s.get("is_editable").value(false);
        is_enabled = s.get("is_enabled").value(0);

        if (value.startsWith(WW.cfg_data_header)) {
            value = Base64Utils.decode(value.substring(8));
        }
        update_fulltime = s.get("update_fulltime").value(null);
    }

    @Override
    public IBinder clone() {
        return new ConfigModel();
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
}