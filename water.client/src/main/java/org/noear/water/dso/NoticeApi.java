package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.WaterConfig;
import org.noear.water.utils.IDUtils;

import java.util.HashMap;
import java.util.Map;

public class NoticeApi {
    /**
     * 嘿嘿通知（经Water服务端处理后再推送）
     *
     * @param target    手机号（多个以,隔开；@alarm 表过报警名单），例：18121212,@alarm
     */
    public String heihei(String target, String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("target", target);
        params.put("msg", msg);


        try {
            String txt = CallUtil.post("run/push/", params);

            System.out.println("NoticeApi::run/push/:" + txt);

            return txt;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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
