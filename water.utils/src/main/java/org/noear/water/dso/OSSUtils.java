package org.noear.water.dso;

import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.HttpUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OSSUtils {
    protected final String bucket;
    protected final String accessKeyId;
    protected final String accessSecret;
    protected final String endpoint;

    protected String CHARSET_UTF8 = "utf8";
    protected String ALGORITHM = "HmacSHA1";

    public OSSUtils(Properties p) {
        this(p, p.getProperty("bucket"));
    }

    public OSSUtils(Properties p, String bucket) {
        this(p.getProperty("endpoint"),
                p.getProperty("accessKeyId"),
                p.getProperty("accessSecret"),
                bucket);
    }

    public OSSUtils(String endpoint, String accessKeyId, String accessSecret, String bucket) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.accessKeyId = accessKeyId;
        this.accessSecret = accessSecret;
    }

    /**
     * 获取对象（string format）
     */
    public String getOssObj(String key) throws Exception {
        String date = Datetime.Now().toGmtString();

        String objPath = "/" + bucket + key;
        String url = "http://" + bucket + "." + endpoint + "/";

        String Signature = (hmacSha1(buildSignData("GET", date, objPath, null), accessSecret));

        String Authorization = "OSS " + accessKeyId + ":" + Signature;

        Map<String, String> head = new HashMap<String, String>();
        head.put("Date", date);
        head.put("Authorization", Authorization);

        return HttpUtils.http(url + key)
                .header("Date", date)
                .header("Authorization", Authorization)
                .get();
    }

    /**
     * 上传对象
     */
    public String putOssObj(String key, String content) throws Exception {
        String date = Datetime.Now().toGmtString();

        String objPath = "/" + bucket + key;
        String url = "http://" + bucket + "." + endpoint + "/";
        String contentType = "text/plain; charset=utf-8";

        String Signature = (hmacSha1(buildSignData("PUT", date, objPath, contentType), accessSecret));
        String Authorization = "OSS " + accessKeyId + ":" + Signature;

        String tmp = HttpUtils.http(url + key)
                .header("Date", date)
                .header("Authorization", Authorization)
                .bodyTxt(content, contentType)
                .put();

        System.out.println(tmp);

        return key;
    }

    /**
     * 上传文件
     */
    public String putOssObj(String key, File file) throws Exception {
        String date = Datetime.Now().toGmtString();

        String objPath = "/" + bucket + key;
        String url = "http://" + bucket + "." + endpoint + "/";
        String contentType = "text/plain; charset=utf-8";

        String Signature = (hmacSha1(buildSignData("PUT", date, objPath, contentType), accessSecret));
        String Authorization = "OSS " + accessKeyId + ":" + Signature;


        String tmp = HttpUtils.http(url + key)
                .header("Date", date)
                .header("Authorization", Authorization)
                .bodyRaw(new FileInputStream(file), contentType)
                .put();

        System.out.println(tmp);

        return key;
    }


    private String hmacSha1(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(data.getBytes(CHARSET_UTF8));

            return Base64Utils.encodeByte(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String buildSignData(String method, String date, String objPath, String contentType) {
        if (contentType == null) {
            return method + "\n\n\n"
                    + date + "\n"
                    + objPath;
        } else {
            return method + "\n\n"
                    + contentType + "\n"
                    + date + "\n"
                    + objPath;
        }
    }
}
