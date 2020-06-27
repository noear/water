package org.noear.water.protocol.solution;

import org.noear.snack.ONode;
import org.noear.water.log.Logger;
import org.noear.water.protocol.Heihei;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;

import java.util.*;

public class HeiheiDefault implements Heihei {
    protected String apiUrl = "https://api.jpush.cn/v3/push";
    protected String masterSecret = "4a8cd168ca71dabcca306cac";
    protected String appKey = "af9a9da3c73d23aa30ea4af1";

    protected final Logger log_heihei;

    public HeiheiDefault(Logger logger){
        log_heihei = logger;
        log_heihei.setName("water_log_heihei");
    }


    public String push(String tag, Collection<String> alias, String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        if (alias == null || alias.size() == 0) {
            return null;
        }

        ONode data = new ONode().build((d) -> {
            d.get("platform").val("all");

            d.get("audience").get("alias").addAll(alias);

            d.get("options")
                    .set("apns_production", true);

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
            return HttpUtils.http(apiUrl)
                    .headers(headers)
                    .bodyTxt(message, "application/json")
                    .post();
        } catch (Exception ex) {
            ex.printStackTrace();
            log_heihei.error(tag, "", ex);
        }

        if (text.startsWith("报警：服务=") == false) {
            log_heihei.info(tag, "", text);
        }

        return null;
    }
}
