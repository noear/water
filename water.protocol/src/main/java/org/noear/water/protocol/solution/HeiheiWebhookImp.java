package org.noear.water.protocol.solution;

import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.protocol.Heihei;
import org.noear.water.utils.HttpResultException;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collection;

/**
 * 嘿嘿的 Webhook 实现
 *
 * @author noear
 * @since 2.7
 */
public class HeiheiWebhookImp implements Heihei {
    private String apiUrl;
    private String accessSecret;

    public HeiheiWebhookImp(String apiUrl, String accessSecret) {
        this.apiUrl = apiUrl;
        this.accessSecret = accessSecret;
    }

    protected final Logger log_heihei = LoggerFactory.getLogger(WW.logger_water_log_heihei);

    @Override
    public String push(String tag, Collection<String> alias, String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        ONode oNode = new ONode();

        oNode.set("msgtype", "text");
        oNode.getOrNew("text").set("content", content);

        try {
            String url;
            if(TextUtils.isEmpty(accessSecret)){
                //如果没有密钥
                url = apiUrl;
            }else{
                long timeStamp = System.currentTimeMillis();
                url = apiUrl + "&timestamp=" + timeStamp + "&sign=" + sign(timeStamp);
            }

            String rst = HttpUtils.shortHttp(url)
                    .bodyJson(oNode.toJson())
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

    private String sign(Long timeStamp) throws Exception {
        String stringToSign = timeStamp + "\n" + accessSecret;

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));

        return new String(Base64.getEncoder().encode(signData));
    }
}