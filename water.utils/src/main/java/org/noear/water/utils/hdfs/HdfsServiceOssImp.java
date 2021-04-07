package org.noear.water.utils.hdfs;

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

/**
 * @author noear 2021/4/7 created
 */
public class HdfsServiceOssImp implements HdfsService {
    protected final String bucket;
    protected final String accessKeyId;
    protected final String accessSecret;
    protected final String endpoint;

    protected String CHARSET_UTF8 = "utf8";
    protected String ALGORITHM = "HmacSHA1";

    public HdfsServiceOssImp(Properties p) {
        this(p, p.getProperty("bucket"));
    }

    public HdfsServiceOssImp(Properties p, String bucket) {
        this(p.getProperty("endpoint"),
                p.getProperty("accessKeyId"),
                p.getProperty("accessSecret"),
                bucket);
    }

    public HdfsServiceOssImp(String endpoint, String accessKeyId, String accessSecret, String bucket) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.accessKeyId = accessKeyId;
        this.accessSecret = accessSecret;
    }

    /**
     * 获取对象（string format）
     */
    @Override
    public String getObj(String key) throws Exception {
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
    @Override
    public String putObj(String key, String content) throws Exception {
        String date = Datetime.Now().toGmtString();

        String objPath = "/" + bucket + key;
        String url = "http://" + bucket + "." + endpoint + "/";
        String contentType = "text/plain; charset=utf-8";

        String Signature = (hmacSha1(buildSignData("PUT", date, objPath, contentType), accessSecret));
        String Authorization = "OSS " + accessKeyId + ":" + Signature;

        return HttpUtils.http(url + key)
                .header("Date", date)
                .header("Authorization", Authorization)
                .bodyTxt(content, contentType)
                .put();
    }

    /**
     * 上传文件
     */
    @Override
    public String putObj(String key, File file) throws Exception {
        String date = Datetime.Now().toGmtString();

        String objPath = "/" + bucket + key;
        String url = "http://" + bucket + "." + endpoint + "/";
        String contentType = "text/plain; charset=utf-8";

        String Signature = (hmacSha1(buildSignData("PUT", date, objPath, contentType), accessSecret));
        String Authorization = "OSS " + accessKeyId + ":" + Signature;


        return HttpUtils.http(url + key)
                .header("Date", date)
                .header("Authorization", Authorization)
                .bodyRaw(new FileInputStream(file), contentType)
                .put();
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

    private static String buildSignData(String method, String date, String objPath, String contentType) {
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
