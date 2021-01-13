package org.noear.water.dso;

import org.noear.water.WaterSetting;
import org.noear.water.utils.HttpUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 用于配置的调用工具
 *
 * @author noear
 * @since 2.0
 * */
class CallCfgUtil {
    public static HttpUtils http(String path) {
        return WaterSetting.water_cfg_upstream().http(path);
    }

    public static String post(String path, Map<String, String> data) throws IOException {
        return http(path).data(data).post();
    }

    public static String postBody(String path, byte[] bytes, String contentType) throws IOException {
        return http(path).bodyRaw(bytes, contentType).post();
    }

    public static void postAsync(String path, Map<String, String> data) {
        try {
            http(path).data(data).postAsync(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String get(String path) throws IOException {
        return http(path).get();
    }
}
