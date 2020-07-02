package org.noear.water.dso;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterConfig;
import org.noear.water.utils.HttpUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

class CallCfgUtil {
    public static HttpUtils http(String path){
        return WaterConfig.water_cfg_upstream().xcall(path);
    }

    public static String post(String path, Map<String, String> data) throws IOException {
        return http(path).data(data).header(WW.http_header_from, WaterClient.localServiceHost()).post();
    }

    public static String postBody(String path, byte[] bytes, String contentType) throws IOException {
        return http(path).bodyRaw(bytes, contentType).header(WW.http_header_from, WaterClient.localServiceHost()).post();
    }

    public static void postAsync(String path, Map<String, String> data)  {
        try {
            http(path).data(data).header(WW.http_header_from, WaterClient.localServiceHost()).postAsync(null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String get(String path) throws IOException {
        return http(path).header(WW.http_header_from, WaterClient.localServiceHost()).get();
    }
}
