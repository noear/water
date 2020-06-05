package org.noear.water.utils;

import org.noear.snack.ONode;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JPushUtils {

    private static final String apiUrl = "https://api.jpush.cn/v3/push";
    private final String appKey;
    private final String masterSecret;


    public JPushUtils(String appKey, String masterSecret) {
        this.appKey = appKey;
        this.masterSecret = masterSecret;
    }

    public String push(Collection<String> alias, String text, ONode data) throws IOException {
        String message = data.toJson();
        String author = Base64Utils.encode(appKey + ":" + masterSecret);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + author);

        return HttpUtils.http(apiUrl)
                .headers(headers)
                .bodyTxt(message, "application/json")
                .post();

    }
    /**
     *      ONode data = new ONode().build((d) -> {
     *             d.get("platform").val("all");
     *
     *             d.get("audience").get("alias").addAll(alias);
     *
     *             d.get("options")
     *                     .set("apns_production", true);
     *
     *             d.get("notification").build(n -> {
     *                 n.get("android")
     *                         .set("alert", text);
     *
     *                 n.get("ios")
     *                         .set("alert", text)
     *                         .set("badge", 0)
     *                         .set("sound", "happy");
     *             });
     *
     *             d.get("message").build(n -> {
     *                 n.set("msg_content", text);
     *             });
     *         });
     * */
}
