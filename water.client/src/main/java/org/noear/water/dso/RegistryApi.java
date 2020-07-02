package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.model.DiscoverM;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册服务（使用 CallCfgUtil）
 * */
public class RegistryApi {
    public void add(String service, String address, String check_url, String alarm_mobile, boolean is_unstable) {
        add(service, address, check_url, 0, alarm_mobile, is_unstable);
    }

    //@parme checkType: 0通过check_url检查，1自己定时签到
    //
    public void add(String service, String address, String check_url, int check_type, String alarm_mobile, boolean is_unstable) {
        add(service, address, "", check_url, check_type, alarm_mobile, is_unstable);
    }

    public void add(String service, String address, String note, String check_url, int check_type, String alarm_mobile, boolean is_unstable) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("address", address);
        params.put("note", note);
        params.put("alarm_mobile", alarm_mobile);
        params.put("is_unstable", (is_unstable ? "1" : "0"));


        params.put("check_url", check_url);
        params.put("check_type", check_type + "");

        try {
            CallCfgUtil.postAsync("sev/reg/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置启用状态
     */
    public void set(String service, String address, String note, boolean enabled) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("address", address);
        params.put("note", note);
        params.put("enabled", (enabled ? "1" : "0"));

        try {
            CallCfgUtil.postAsync("sev/set/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DiscoverM discover(String service, String consumer, String consumer_address) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("consumer", consumer);
        params.put("consumer_address", consumer_address);

        try {

            String json = CallCfgUtil.post("/sev/discover/", params);
            ONode data = ONode.loadStr(json).get("data");

            if (data.isObject()) {
                DiscoverM cfg = new DiscoverM();

                cfg.url = data.get("url").getString();
                cfg.policy = data.get("policy").getString();

                if (data.contains("list")) {
                    for (ONode n : data.get("list").ary()) {
                        cfg.add(n.get("protocol").getString(),
                                n.get("address").getString(),
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
}
