package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.WaterConfig;
import org.noear.water.utils.IDUtils;

import java.util.HashMap;
import java.util.Map;

public class ToolApi {

    /**
     * 检测，是否为白名单
     *
     * @param tags   分组(多个以,隔开)
     * @param type  类型(ip,mobile,host)
     * @param value 值
     */
    public boolean isWhitelist(String tags, String type, String value) {
        Map<String, String> params = new HashMap<>();
        params.put("tags", tags);
        params.put("type", type);
        params.put("value", value);

        try {
            return "OK".equals(CallUtil.post("run/whitelist/check/", params));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    /**
     * 通知缓存更新
     */
    public void updateCache(String tags) {
        //tags以;隔开

        try {
            WaterClient.Message.sendMessage(IDUtils.guid(), WaterConfig.msg_ucache_topic, tags);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 通知配置更新
     */
    public void updateConfig(String tag, String name) {
        try {
            WaterClient.Message.sendMessage(IDUtils.guid(), WaterConfig.msg_uconfig_topic, tag + "::" + name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
