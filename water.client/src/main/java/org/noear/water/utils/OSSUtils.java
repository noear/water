package org.noear.water.utils;

import org.noear.water.model.ConfigM;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于Http api 的OSS工具类
 * */
public class OSSUtils {
    private String bucket;
    private String endpoint;
    private String accessKeyId;
    private String accessSecretKey;

    private static Map<String, org.noear.water.dso.OSSUtils> cacheMap = new ConcurrentHashMap<>();
    public static org.noear.water.dso.OSSUtils get(ConfigM cfg){
        org.noear.water.dso.OSSUtils tmp = cacheMap.get(cfg.value);
        if(tmp == null){
            tmp = new org.noear.water.dso.OSSUtils(cfg.getProp());
            cacheMap.putIfAbsent(cfg.value,tmp);
        }
        return tmp;
    }

    public OSSUtils(Properties prop) {
        this(prop.getProperty("bucket"),
                prop.getProperty("endpoint"),
                prop.getProperty("accessKeyId"),
                prop.getProperty("accessSecretKey"));
    }

    public OSSUtils(String bucket, String endpoint, String accessKeyId, String accessSecretKey) {
        this.bucket = bucket;
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessSecretKey = accessSecretKey;
    }

    public void putFile(String objName, InputStream content, String contentType) throws Exception {
        HttpUtils client = this.buildRequest(objName, contentType, "PUT");
        client.bodyRaw(content, contentType).put();
    }

    public void putString(String objName, String content) throws Exception {
        HttpUtils client = this.buildRequest(objName, null, "PUT");
        client.bodyTxt(content, null).put();
    }

    public String getString(String objName) throws Exception {
        HttpUtils client = this.buildRequest(objName, null, "GET");
        return client.get();
    }

    private HttpUtils buildRequest(String objName, String contentType, String method) {
        String objPath = "/" + bucket + '/' + objName;

        String url = "http://" + bucket + "." + endpoint + '/';

        String date = Datetime.Now().toGmtString();

        String sign_data = signData(method, date, objPath, contentType);
        String sign = EncryptUtils.hmacSha1(sign_data, accessSecretKey);

        String auth = "OSS " + accessKeyId + ":" + sign;

        HttpUtils http = HttpUtils.http(url + objName);

        http.header("Date", date).header("Authorization", auth);

        return http;
    }

    private String signData(String method, String date, String objPath, String contentType) {
        if (TextUtils.isEmpty(contentType)) {
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
