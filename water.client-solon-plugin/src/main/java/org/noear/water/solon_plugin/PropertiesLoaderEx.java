package org.noear.water.solon_plugin;

import org.noear.solon.XUtil;
import org.noear.water.model.PropertiesM;
import org.noear.water.utils.PropertiesLoader;

import java.util.Properties;

public class PropertiesLoaderEx extends PropertiesLoader {
    @Override
    public PropertiesM load(String text) throws Exception {
        PropertiesM prop = new PropertiesM();
        Properties tmp = XUtil.getProperties(text);

        if (tmp != null) {
            prop.putAll(tmp);
        }

        return prop;
    }
}
