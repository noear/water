package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.WaterClient;
import org.noear.water.model.DiscoverM;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注册服务（使用 CallCfgUtil）
 *
 * @author noear
 * @since 2.0
 * */
public class RegistryApi {
    protected final ApiCaller apiCaller;

    public RegistryApi() {
        apiCaller = new ApiCaller(WaterAddress.getRegistryApiUrl());
    }

    private Map<String, Set<DiscoverHandler>> _event = new HashMap<>();

    /**
     * 注册（用于对接外部框架）
     */
    public void register(String tag, String service, String address, String meta, boolean is_unstable) {
        register(tag, service, address, meta, "", 1, "", is_unstable);
    }

    /**
     * 注册
     */
    public void register(String tag, String service, String address, String check_url, String alarm_mobile, boolean is_unstable) {
        register(tag, service, address, check_url, 0, alarm_mobile, is_unstable);
    }

    /**
     * 注册
     *
     * @param check_type 0:通过check_url检查，1:自己定时签到
     */
    public void register(String tag, String service, String address, String check_url, int check_type, String alarm_mobile, boolean is_unstable) {
        register(tag, service, address, "", check_url, check_type, alarm_mobile, is_unstable);
    }

    public void register(String tag, String service, String address, String meta, String check_url, int check_type, String alarm_mobile, boolean is_unstable) {
        String code_location = WaterClient.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        register(tag, service, address, meta, check_url, check_type, alarm_mobile, code_location, is_unstable);
    }

    /**
     * 注册
     */
    public void register(String tag, String service, String address, String meta, String check_url, int check_type, String alarm_mobile, String code_location, boolean is_unstable) {
        if (tag == null) {
            tag = "";
        }

        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("address", address);
        params.put("meta", meta);
        params.put("tag", tag);
        params.put("alarm_mobile", alarm_mobile);
        params.put("is_unstable", (is_unstable ? "1" : "0")); //用于兼容k8s的ip漂移

        params.put("code_location", code_location);

        params.put("check_url", check_url);
        params.put("check_type", check_type + "");

        try {
            apiCaller.post("/sev/reg/", params);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 注销（用于对接外部框架）
     */
    public void unregister(String tag, String service, String address, String meta) {
        Map<String, String> params = new HashMap<>();

        params.put("tag", tag);
        params.put("service", service);
        params.put("address", address);
        params.put("meta", meta);

        try {
            apiCaller.post("/sev/unreg/", params);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置启用状态
     */
    public void set(String tag, String service, String address, String meta, boolean enabled) {
        Map<String, String> params = new HashMap<>();

        params.put("tag", tag);
        params.put("service", service);
        params.put("address", address);
        params.put("meta", meta);
        params.put("enabled", (enabled ? "1" : "0"));

        try {
            apiCaller.post("/sev/set/", params);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 发现
     */
    public DiscoverM discover(String tag, String service, String consumer, String consumer_address) {
        return load0(tag, service, consumer, consumer_address);
    }

    private DiscoverM load0(String tag, String service, String consumer, String consumer_address) {
        Map<String, String> params = new HashMap<>();

        params.put("tag", tag);
        params.put("service", service);
        params.put("consumer", consumer);
        params.put("consumer_address", consumer_address);

        try {

            String json = apiCaller.post("/sev/discover/", params);
            ONode rst = ONode.loadStr(json);
            int code = rst.get("code").getInt();

            if (code != 1 && code != 200) {
                return null;
            }

            ONode data = rst.get("data");

            if (data.isObject()) {
                DiscoverM cfg = new DiscoverM();

                cfg.url = data.get("url").getString();
                cfg.policy = data.get("policy").getString();

                if (data.contains("list")) {
                    for (ONode n : data.get("list").ary()) {
                        cfg.add(n.get("protocol").getString(),
                                n.get("address").getString(),
                                n.get("meta").getString(),
                                n.get("weight").getInt());
                    }
                }
                return cfg;
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 发现刷新
     */
//    public void discoverFlush(String tag, String service, String consumer, String consumer_address) {
//        DiscoverM d1 = load0(tag, service, consumer, consumer_address);
//        noticeTry(tag, service, d1);
//    }
//
//    private void noticeTry(String tag, String service, DiscoverM discover) {
//        String discoverKey = tag + "/" + service;
//
//        Set<DiscoverHandler> tmp = _event.get(discoverKey);
//
//        if (tmp != null) {
//            for (DiscoverHandler r : tmp) {
//                r.handler(discover);
//            }
//        }
//    }

    /**
     * 订阅服务
     * */
//    public void subscribe(String tag, String service, DiscoverHandler callback) {
//        String discoverKey = tag + "/" + service;
//
//        Set<DiscoverHandler> tmp = _event.get(discoverKey);
//        if (tmp == null) {
//            tmp = new HashSet<>();
//            _event.put(service, tmp);
//        }
//
//        tmp.add(callback);
//    }
}