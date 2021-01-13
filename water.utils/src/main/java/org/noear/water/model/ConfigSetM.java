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

    /**
     * 检测是否存在
     */
    public boolean has(String key) {
        return _map.containsKey(key);
    }

    /**
     * 设置
     */
    public void set(String key, String value) {
        _map.put(key, new ConfigM(key, value, 0));
    }

    /**
     * 遍历配置项
     */
    public void forEach(BiConsumer<String, ConfigM> action) {
        _map.forEach(action);
    }

    /**
     * 加载配置数据
     */
    public void load(ONode node) {
        int code = node.get("code").getInt();

        if (code == 1) {
            node.get("data").forEach((k, v) -> {
                ConfigM val = new ConfigM(
                        v.get("key").getString(),
                        v.get("value").getString(),
                        v.get("lastModified").getLong());

                _map.put(k, val);
            });
        } else {
            System.err.println(node.toJson());
        }
    }

    private Properties _propSet;

    /**
     * 获取一个Properties集合
     */
    public Properties getPropSet() {
        if (_propSet == null) {
            _propSet = new Properties();

            //将@开头的变量，转到系统
            _map.forEach((k, v) -> {
                if (v.value != null) {

                    String keyTmp = null;

                    //确定key
                    if (k.startsWith("@")) {
                        keyTmp = k.substring(1);
                    } else {
                        keyTmp = _tag + "." + k;
                    }

                    String key = keyTmp;

                    //开始写数据
                    putTo(key, v, _propSet);
                }
            });
        }

        return _propSet;
    }

    /**
     * 同步到系统
     */
    public void sync() {
        _map.forEach((k, v) -> {
            if (v != null && k.startsWith("@@")) {
                //确定key
                String key = k.substring(2);

                //开始写数据
                putTo(key, v, System.getProperties());
            }
        });
    }

    private void putTo(String key, ConfigM val, Properties target) {
        if (val.value.indexOf("=") < 0) {
            target.setProperty(key, val.value);
        } else {
            PropertiesM prop = val.getProp();
            prop.forEach((k1, v1) -> {
                if (v1 != null) {
                    //支持块内的宏模式::by noear, 2021.01.13
                    //url=xxxxxx
                    //jdbcUrl=${url}
                    //
                    String v2 = v1.toString();
                    if (v2.startsWith("${") && v2.endsWith("}")) {
                        v2 = prop.getProperty(v2.substring(2, v2.length() - 1));
                    }

                    target.setProperty(key + "." + k1, v2);
                }
            });
        }
    }
}
