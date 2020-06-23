package org.noear.water.utils;

import org.noear.water.model.PropertiesM;

import java.io.StringReader;

public class PropertiesLoader {
    public static PropertiesLoader global = new PropertiesLoader();

    static {
        String loaderEx = "org.noear.water.solon_plugin.PropertiesLoaderEx";

        try {
            Class<?> clz = Class.forName(loaderEx);
            if (clz != null) {
                Object tmp = clz.newInstance();
                global = (PropertiesLoader) tmp;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PropertiesM load(String text) throws Exception {
        PropertiesM m = new PropertiesM();
        m.load(new StringReader(text));
        return m;
    }
}