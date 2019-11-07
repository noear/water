package org.noear.water.client.dso;

import org.noear.snack.ONode;
import org.noear.water.client.model.ConfigM;

import java.util.*;

/**
 * 配置接口
 *
 * config pk = tag + key
 * */
public class ConfigApi {

    private Map<String, ONode> _cfgs = Collections.synchronizedMap(new HashMap());

    private void do_tryInit(String tag) {
        synchronized (_cfgs) {
            if (_cfgs.containsKey(tag)) {
                return;
            }

            do_load(tag);
        }
    }

    private void do_load(String tag) {
        try {
            String temp = WaterApi.get("/cfg/?tag=" + tag);
            ONode conf = ONode.loadStr(temp);
            _cfgs.put(tag, conf);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 重新加载
     */
    public void reload(String tag) {
        if (_cfgs.containsKey(tag) == false) {
            return;
        }

        do_load(tag);
    }

    /**
     * 获取配置
     */
    public ConfigM get(String tag, String key) {
        do_tryInit(tag);

        ONode _data = _cfgs.get(tag);

        if (_data.contains(key)) {
            ONode n = _data.get(key);
            return new ConfigM(n.get("key").getString(),
                    n.get("value").getString(),
                    n.get("lastModified").getLong());
        } else {
            return new ConfigM();
        }
    }

    /**
     * 获取配置
     */
    public ConfigM getByTagKey(String tagKey) {
        String[] ss = tagKey.split("/");
        return get(ss[0], ss[1]);
    }
}
