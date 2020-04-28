package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.model.ConfigM;
import org.noear.water.model.ConfigSetM;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置服务接口
 * */
public class ConfigApi {

    private Map<String, ConfigSetM> _cfgs = Collections.synchronizedMap(new HashMap());

    private void tryInit(String tag) {
        synchronized (_cfgs) {
            if (_cfgs.containsKey(tag)) {
                return;
            }

            load(tag);
        }
    }

    private void load(String tag) {
        ConfigSetM cfgSet = new ConfigSetM(tag);

        try {
            String temp = CallUtil.get("cfg/get/?v=2&tag=" + tag);
            cfgSet.load(ONode.loadStr(temp));
        } catch (Exception ex) {
            ex.printStackTrace();
            //
            // 如果加载失败，且已存在；直接返回
            //
            if (_cfgs.containsKey(tag)) {
                return;
            }
        }

        _cfgs.put(tag, cfgSet);
    }

    /**
     * 重新加载一个tag的配置
     */
    public void reload(String tag) {
        if (_cfgs.containsKey(tag) == false) {
            return;
        }

        load(tag);
    }

    /**
     * 获取系统配置
     */
    public Properties getProperties(String tag) {
        tryInit(tag);
        return _cfgs.get(tag).getPropSet();
    }

    /**
     * 获取配置，根据tag/key
     */
    public ConfigM getByTagKey(String tagKey) {
        String[] ss = tagKey.split("/");
        return get(ss[0], ss[1]);
    }


    /**
     * 获取配置（不会返回null）
     */
    public ConfigM get(String tag, String key) {
        tryInit(tag);

        return _cfgs.get(tag).get(key);
    }


    /**
     * 设置配置
     */
    public void set(String tag, String key, String value) throws IOException {

        CallUtil.http("cfg/set/")
                .data("tag", tag)
                .data("key", key)
                .data("value", value)
                .post();

    }
}
