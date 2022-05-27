package org.noear.water.protocol.solution;

import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.protocol.Heihei;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.HttpResultException;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 嘿嘿的极光推送实现
 * */
public class HeiheiDefaultImp implements Heihei {
    private String apiUrl = "https://api.jpush.cn/v3/push";
    private String accessKeyId = "af9a9da3c73d23aa30ea4af1";
    private String accessSecret = "4a8cd168ca71dabcca306cac";

    protected final Logger log_heihei = LoggerFactory.getLogger(WW.logger_water_log_heihei);


    public String push(String tag, Collection<String> alias, String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        if (alias == null || alias.size() == 0) {
            return null;
        }

        ONode data = new ONode().build((d) -> {
            d.getOrNew("platform").val("all");

            d.getOrNew("audience").getOrNew("alias").addAll(alias);

            d.getOrNew("options")
                    .set("apns_production", true);

            d.getOrNew("notification").build(n -> {
                n.getOrNew("android")
                        .set("alert", content);

                n.getOrNew("ios")
                        .set("alert", content)
                        .set("badge", 0)
                        .set("sound", "happy");
            });

            d.getOrNew("message").build(n -> {
                n.set("msg_content", content);
            });
        });


        String message = data.toJson();
        String author = Base64Utils.encode(accessKeyId + ":" + accessSecret);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + author);

        MDC.put("tag0", tag);

        try {
            String rst = HttpUtils.shortHttp(apiUrl)
                    .headers(headers)
                    .bodyJson(message)
                    .post();


            log_heihei.info(content);

            return rst;
        } catch (HttpResultException ex) {
            log_heihei.warn("{}", ex.getLocalizedMessage());
        } catch (Exception ex) {
            log_heihei.error("{}", ex);
        }

        return null;
    }
}
