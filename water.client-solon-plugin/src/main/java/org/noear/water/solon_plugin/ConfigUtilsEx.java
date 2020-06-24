package org.noear.water.solon_plugin;

import org.noear.solon.XUtil;
import org.noear.water.model.PropertiesM;
import org.noear.water.utils.ConfigUtils;

import java.util.Properties;

public class ConfigUtilsEx extends ConfigUtils {
    @Override
    public PropertiesM getProp(String text) {
        PropertiesM prop = new PropertiesM();
        Properties tmp = XUtil.getProperties(text);

        if (tmp != null) {
            prop.putAll(tmp);
        }

        return prop;
    }
}
