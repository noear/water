package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.model.ConfigM;
import org.noear.water.model.ConfigSetM;

import java.io.IOException;
import java.util.*;

/**
 * 配置服务接口（使用 CallCfgUtil）
 *
 * @author noear
 * @since 2.0
 * */
public class ConfigApi {

    private Map<String, ConfigSetM> _cfgs = Collections.synchronizedMap(new HashMap());
    private Map<String, Set<ConfigHandler>> _event = new HashMap<>();

    protected final CallUtils callUtils;
    public ConfigApi(){
        callUtils = new CallUtils(WaterAddress.getInstance().getConfigApiUrl());
    }

    /**
     * 重新加载一个tag的配置
     */
    public void reload(String tag) {
        synchronized (_cfgs) {
            if (_cfgs.containsKey(tag) == false) {
                return;
            }

            load0(tag);
        }
    }

    /**
     * 加载一个tag的配置
     */
    public void load(String tag) {
        synchronized (_cfgs) {
            if (_cfgs.containsKey(tag)) {
                return;
            }

            load0(tag);
        }
    }

    private void load0(String tag) {
        ConfigSetM cfgSet = new ConfigSetM(tag);

        try {
            String temp = callUtils.get("/cfg/get/?v=2&tag=" + tag);
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

        //尝试通知订阅者
        noticeTry(tag, cfgSet);
    }




    /**
     * 获取系统配置
     */
    public Properties getProperties(String tag) {
        load(tag);
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
        load(tag);

        return _cfgs.get(tag).get(key);
    }

    private void noticeTry(String target, ConfigSetM cfgSet){
        Set<ConfigHandler> tmp = _event.get(target);

        if (tmp != null) {
            for (ConfigHandler r : tmp) {
                r.handler(cfgSet);
            }
        }
    }

    /**
     * 订阅配置集
     */
    public void subscribe(String tag, ConfigHandler callback) {
        Set<ConfigHandler> tmp = _event.get(tag);
        if (tmp == null) {
            tmp = new HashSet<>();
            _event.put(tag, tmp);
        }

        tmp.add(callback);

        //如果已存在，及时通知
        if (_cfgs.containsKey(tag)) {
            callback.handler(_cfgs.get(tag));
        }
    }


    /**
     * 设置配置，根据tag/key
     */
    public void setByTagKey(String tagKey, String value) throws IOException {
        String[] ss = tagKey.split("/");
        set(ss[0], ss[1], value);
    }

    /**
     * 设置配置
     */
    public void set(String tag, String key, String value) throws IOException {

        callUtils.http("/cfg/set/")
                .data("tag", tag)
                .data("key", key)
                .data("value", value)
                .post();


        if (_cfgs.containsKey(tag)) {
            _cfgs.get(tag).set(key, value);
        }
    }
}
