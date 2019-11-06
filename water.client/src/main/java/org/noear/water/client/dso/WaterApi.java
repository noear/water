package org.noear.water.client.dso;

import org.noear.water.client.WaterConfig;
import org.noear.water.tools.HttpUtils;

import java.util.Map;

public class WaterApi {
    public static String get(String path) throws Exception {
        String url = WaterConfig.water_host + path;

        return HttpUtils.http(url).get();
    }

    public static String post(String path, Map<String, String> data) throws Exception {
        String url = WaterConfig.water_host + path;

        return HttpUtils.http(url).data(data).post();
    }


    public static void postAsync(String path, Map<String, String> data) throws Exception {
        String url = WaterConfig.water_host + path;

        HttpUtils.http(url).data(data).post();
    }
}
