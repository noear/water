package org.noear.water.client.dso;

import org.noear.water.client.WaterClient;
import org.noear.water.client.WaterConfig;
import org.noear.water.tools.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class ToolApi {
    //
    // target:手机号（多个以,隔开；@alarm 表过报警联系人）：18121212,@alarm
    //
    public static String pushHeihei(String accessKey, String target, String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("target", target);
        params.put("msg", msg);

        if (TextUtils.isEmpty(accessKey) == false) {
            params.put("ak", accessKey);
        }

        try {
            return WaterApi.post("/run/push/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String pushHeihei(String target, String msg) {
        return pushHeihei("86427448ab8740a381f4439dafef5c72", target, msg);
    }

    public static void updateCache(String tags) {
        //tags以;隔开

        try {
            WaterClient.Message.messageSend(WaterConfig.msg_ucache_topic, tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void updateConfig(String tag, String name) {
        try {
            WaterClient.Message.messageSend(WaterConfig.msg_uconfig_topic, tag + "::" + name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
