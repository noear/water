package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.WaterConfig;
import org.noear.water.utils.HttpUtils;

import java.io.IOException;
import java.util.Map;

class CallUtil {
    public static HttpUtils http(String path){
        return HttpUtils.http(WaterConfig.water_api_url() + path);
    }

    public static String post(String path, Map<String, String> data) throws IOException {
        return http(path).data(data).header("_from", WaterClient.serviceAddr()).post();
    }

    public static void postAsync(String path, Map<String, String> data)  {
        try {
            http(path).data(data).header("_from", WaterClient.serviceAddr()).postAsync(null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String get(String path) throws IOException {
        return http(path).header("_from", WaterClient.serviceAddr()).get();
    }
}
