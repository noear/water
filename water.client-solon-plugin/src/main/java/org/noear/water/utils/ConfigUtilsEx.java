package org.noear.water.utils;

import org.noear.solon.Utils;
import org.noear.water.model.PropertiesM;
import org.noear.water.utils.ConfigUtils;

import java.util.Properties;

/**
 * 配置加载工具
 *
 * @author noear
 * @since 2.0
 * */
public class ConfigUtilsEx extends ConfigUtils {
    @Override
    public PropertiesM getProp(String text) {
        PropertiesM prop = new PropertiesM();
        Properties tmp = Utils.buildProperties(text);

        if (tmp != null) {
            prop.putAll(tmp);
        }

        return prop;
    }
}
