package org.noear.water.model;

import org.noear.snack.ONode;

import java.util.HashMap;
import java.util.Properties;
import java.util.function.BiConsumer;

public class ConfigSetM {
    private String _tag = null;
    private ConfigM _empty = new ConfigM();
    private HashMap<String, ConfigM> _map = new HashMap<>();

    public ConfigSetM(String tag) {
        _tag = tag;
    }

    /**
     * 获取配置项
     */
    public ConfigM get(String key) {
        return _map.getOrDefault(key, _empty);
    }

    public void set(String key, String value) {
        _map.put(key, new ConfigM(key, value, 0));
    }

    /**
     * 遍历配置项
     */
    public void forEach(BiConsumer<String, ConfigM> action) {
        _map.forEach(action);
    }

    public void load(ONode node) {
        int code = node.get("code").getInt();

        if (code == 1) {
            node.get("data").forEach((k, v) -> {
                ConfigM val = new ConfigM(
                        v.get("key").getString(),
                        v.get("value").getString(),
                        v.get("lastModified").getLong());

                _map.put(k, val);

                if (k.startsWith("@@")) {
                    System.getProperty(val.key.substring(2), val.value);
                }
            });
        } else {
            System.err.println(node.toJson());
        }
    }

    private Properties _propSet;

    public Properties getPropSet() {
        if (_propSet == null) {
            _propSet = new Properties();

            //将@开头的变量，转到系统
            _map.forEach((k, v) -> {
                if (v.value != null) {

                    String keyTmp = null;

                    //确定key
                    if (k.startsWith("@")) {
                        if (k.startsWith("@@")) {
                            keyTmp = k.substring(2);
                        } else {
                            keyTmp = k.substring(1);
                        }
                    } else {
                        keyTmp = _tag + "." + k;
                    }

                    String key = keyTmp;

                    //开始写数据
                    if (v.value.indexOf("=") < 0) {
                        _propSet.setProperty(key, v.value);
                    } else {
                        v.getProp().forEach((k1, v1) -> {
                            if (v1 != null) {
                                _propSet.setProperty(key + "." + k1, v1.toString());
                            }
                        });
                    }
                }
            });
        }

        return _propSet;
    }
}
