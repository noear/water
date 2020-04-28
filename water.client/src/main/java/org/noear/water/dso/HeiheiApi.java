package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.log.Logger;
import org.noear.water.log.WaterLogger;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;

import java.util.*;

/**
 * 嘿嘿通知接口
 * */
public class HeiheiApi {

    private static final Logger log_heihei = new WaterLogger("water_log_heihei");

    private static final String apiUrl = "https://api.jpush.cn/v3/push";
    private static final String masterSecret = "4a8cd168ca71dabcca306cac";
    private static final String appKey = "af9a9da3c73d23aa30ea4af1";


    /**
     * 嘿嘿推送
     *
     * @param tag             日志记录标签
     * @param alias           别名列表
     * @param apns_production 是否推生产环境s
     */
    public void push(String tag, List<String> alias, String text, boolean apns_production) {
        if (alias.size() == 0) {
            return;
        }

        ONode data = new ONode().build((d) -> {
            d.get("platform").val("all");

            d.get("audience").get("alias").addAll(alias);

            d.get("options")
                    .set("apns_production", apns_production);

            d.get("notification").build(n -> {
                n.get("android")
                        .set("alert", text);

                n.get("ios")
                        .set("alert", text)
                        .set("badge", 0)
                        .set("sound", "happy");
            });

            d.get("message").build(n -> {
                n.set("msg_content", text);
            });
        });


        String message = data.toJson();
        String author = Base64Utils.encode(appKey + ":" + masterSecret);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + author);

        try {
            HttpUtils.http(apiUrl)
                    .headers(headers)
                    .bodyTxt(message, "application/json")
                    .post();
        } catch (Exception ex) {
            ex.printStackTrace();
            log_heihei.error("HeiheiApi", "", ex);
        }

        if (text.startsWith("报警") == false && text.startsWith("恢复") == false) {
            log_heihei.info(tag, "", text);
        }
    }

    /**
     * 嘿嘿推送
     */
    public void push(String tag, List<String> alias, String text) {
        push(tag, alias, text, true);
    }


    /**
     * 嘿嘿通知（经Water服务端处理后再推送）
     *
     * @param target    手机号（多个以,隔开；@alarm 表过报警名单），例：18121212,@alarm
     */
    public String notice(String target, String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("target", target);
        params.put("msg", msg);


        try {
            return CallUtil.post("run/push/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
