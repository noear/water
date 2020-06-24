package org.noear.water.utils;

import org.noear.snack.ONode;
import org.noear.water.model.PropertiesM;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;

public class ConfigUtils {
    public static ConfigUtils global = new ConfigUtils();

    static {
        String loaderEx = "org.noear.water.solon_plugin.ConfigUtilsEx";

        try {
            Class<?> clz = Class.forName(loaderEx);
            if (clz != null) {
                Object tmp = clz.newInstance();
                global = (ConfigUtils) tmp;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PropertiesM getProp(String text) {
        try {
            PropertiesM m = new PropertiesM();
            m.load(new StringReader(text));
            return m;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public ONode getNode(String text){
        if(TextUtils.isEmpty(text)){
            return new ONode();
        }

        int idx0 = text.indexOf(":");
        int idx1 = text.indexOf("{");
        int idx2 = text.indexOf("[");

        if(idx0 > 0){
            if(idx1 < 0){
                idx1 = 9999;
            }

            if(idx2 < 0){
                idx2 = 9999;
            }

            if(idx0 < idx1 && idx0 < idx2){
                Yaml yaml = new Yaml();
                Object tmp = yaml.load(new StringReader(text));

                return ONode.loadObj(tmp);
            }
        }

        return ONode.loadStr(text);
    }
}