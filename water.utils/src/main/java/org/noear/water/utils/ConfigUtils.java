package org.noear.water.utils;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.water.model.PropertiesM;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;
import java.util.Properties;

public class ConfigUtils {
    public static ConfigUtils global = new ConfigUtils();

    public PropertiesM getProp(String text) {
        PropertiesM prop = new PropertiesM();
        Properties tmp = Utils.buildProperties(text);

        if (tmp != null) {
            prop.putAll(tmp);
        }

        buildPropOfMacro(prop);

        return prop;
    }

    protected void buildPropOfMacro(PropertiesM prop) {
        prop.forEach((k, v) -> {
            if (v != null) {
                String v2 = v.toString();

                if (v2.startsWith("${") && v2.endsWith("}")) {
                    v2 = prop.getProperty(v2.substring(2, v2.length() - 1));
                    prop.put(k, v2);
                }
            }
        });
    }

    public ONode getNode(String text) {
        if (TextUtils.isEmpty(text)) {
            return new ONode();
        }

        int idx0 = text.indexOf(":");
        int idx1 = text.indexOf("=");

        int idx11 = text.indexOf("{");
        int idx12 = text.indexOf("[");

        if (idx11 < 0) { //没有{
            idx11 = 9999;
        }

        if (idx12 < 0) { //没有[
            idx12 = 9999;
        }

        //尝试检查 yaml 格式
        if (idx0 > 0 && (idx1 < 0 || idx0 < idx1)) { //有:
            //说明是Yaml
            if (idx0 < idx11 && idx0 < idx12) {
                Yaml yaml = new Yaml();
                Object tmp = yaml.load(new StringReader(text));

                return ONode.loadObj(tmp);
            }
        }

        //尝试检查 properties 格式
        if (idx1 > 0 && (idx0 < 0 || idx1 < idx0)) {
            if (idx0 < idx11 && idx0 < idx12) {
                Properties props = new Properties();
                try {
                    props.load(new StringReader(text));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                ONode node = new ONode();

                props.forEach((k, v) -> {
                    if (k instanceof String) {
                        String[] sss = ((String) k).split("\\.");
                        ONode tmp = node;
                        int il = 0, ir = 0;
                        for (String s1 : sss) {
                            il = s1.indexOf("[");
                            if (il < 0) {
                                tmp = tmp.getOrNew(s1);
                            } else {
                                ir = s1.indexOf("]");
                                tmp = tmp.getOrNew(s1.substring(0,il));
                                tmp = tmp.getOrNew(Integer.parseInt(s1.substring(il + 1, ir)));
                            }
                        }
                        tmp.val(v);
                    }
                });

                return node;
            }
        }

        return ONode.loadStr(text);
    }
}