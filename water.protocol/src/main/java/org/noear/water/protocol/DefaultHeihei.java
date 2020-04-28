package org.noear.water.protocol;

import org.noear.snack.ONode;
import org.noear.water.log.Logger;
import org.noear.water.log.LoggerFactory;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpUtils;

import java.util.*;

public class DefaultHeihei implements IHeihei {

    private static final Logger log_heihei = LoggerFactory.get("water_log_heihei");

    private static final String apiUrl = "https://api.jpush.cn/v3/push";
    private static final String masterSecret = "4a8cd168ca71dabcca306cac";
    private static final String appKey = "af9a9da3c73d23aa30ea4af1";

    private static DefaultHeihei _singleton = new DefaultHeihei();

    public static DefaultHeihei singleton() {
        return _singleton;
    }


    public String push(String tag, Collection<String> alias, String text) {
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
            return
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

        return null;
    }
}
