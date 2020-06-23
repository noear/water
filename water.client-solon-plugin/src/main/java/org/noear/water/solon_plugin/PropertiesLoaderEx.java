package org.noear.water.solon_plugin;

import org.noear.solon.XUtil;
import org.noear.water.model.PropertiesM;
import org.noear.water.utils.PropertiesLoader;

import java.util.Properties;

public class PropertiesLoaderEx extends PropertiesLoader {
    @Override
    public PropertiesM load(String text) throws Exception {
        Properties prop = XUtil.getProperties(text);

        if (prop == null) {
            return new PropertiesM();
        } else {
            return new PropertiesM(prop);
        }
    }
}
