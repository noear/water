package org.noear.water.client.dso;

import org.noear.snack.ONode;
import org.noear.water.client.model.DiscoverModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册接口
 * */
public class RegistryApi {
    public static void add(String service, String address, String check_url, String alarm_mobile) {
        add(service, address, check_url, 0, alarm_mobile);
    }

    //@parme checkType: 0通过check_url检查，1自己定时签到
    //
    public static void add(String service, String address, String check_url, int check_type, String alarm_mobile) {
        add(service, address, "", check_url, check_type, alarm_mobile);
    }

    public static void add(String service, String address, String note, String check_url, int check_type, String alarm_mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("address", address);
        params.put("note", note);
        params.put("alarm_mobile", alarm_mobile);


        params.put("check_url", check_url);
        params.put("check_type", check_type + "");

        try {
            WaterApi.postAsync("/sev/reg/",params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 设置启用状态
     */
    public static void set(String service, String address, String note, boolean enabled) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("address", address);
        params.put("note", note);
        params.put("enabled", (enabled ? "1" : "0"));

        try {
            WaterApi.postAsync("sev/set/",params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static DiscoverModel discover(String service, String consumer, String consumer_address) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        params.put("consumer", consumer);
        params.put("consumer_address", consumer_address);

        try {

            String json = WaterApi.post("/sev/discover/",params);

            ONode data = ONode.loadStr(json).get("data");

            DiscoverModel model = new DiscoverModel();
            model.url = data.get("url").getString();
            model.policy = data.get("policy").getString();
            if (data.contains("list")) {
                for (ONode n : data.get("list").ary()) {
                    model.add(n.get("protocol").getString(),
                            n.get("address").getString(),
                            n.get("weight").getInt());
                }
            }
            return model;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
