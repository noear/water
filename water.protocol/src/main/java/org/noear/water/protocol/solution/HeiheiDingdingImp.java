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
import java.util.HashMap;
import java.util.Map;

/**
 * 嘿嘿的叮叮实现
 *
 * @author noear
 * @since 2.7
 */
public class HeiheiDingdingImp implements Heihei {
    private String apiUrl;
    private String accessSecret;

    public HeiheiDingdingImp(String apiUrl, String accessSecret) {
        this.apiUrl = apiUrl;
        this.accessSecret = accessSecret;
    }

    protected final Logger log_heihei = LoggerFactory.getLogger(WW.logger_water_log_heihei);

    @Override
    public String push(String tag, Collection<String> alias, String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        long timeStamp = System.currentTimeMillis();

        Map<String, Object> json = new HashMap(3);
        Map<String, Object> text = new HashMap(3);
        json.put("msgtype", "text");
        text.put("content", content);
        json.put("text", text);

        try {
            String url = apiUrl + "&timestamp=" + timeStamp + "&sign=" + sign(timeStamp);

            String rst = HttpUtils.shortHttp(url)
                    .bodyJson(ONode.stringify(json))
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

    /**
     * 钉钉签名
     */
    private String sign(Long timeStamp) throws Exception {
        String stringToSign = timeStamp + "\n" + accessSecret;

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));

        return new String(Base64.getEncoder().encode(signData));
    }
}