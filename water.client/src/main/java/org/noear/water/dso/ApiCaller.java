package org.noear.water.dso;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.utils.HttpUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 用于服务的调用工具
 *
 * @author noear
 * @since 2.0
 * */
public class ApiCaller {
    private String server;
    public ApiCaller(String server){
        this.server = server;
    }

    public HttpUtils http(String path) {
        String url;
        if (server.endsWith("/")) {
            if (path.startsWith("/")) {
                url = server + path.substring(1);
            } else {
                url = server + path;
            }
        } else {
            if (path.startsWith("/")) {
                url = server + path;
            } else {
                url = server + "/" + path;
            }
        }

        return HttpUtils.http(url)
                .headerAdd(WW.http_header_trace, WaterClient.waterTraceId())
                .headerAdd(WW.http_header_from, WaterClient.localServiceHost());
    }

    public  String post(String path, Map<String, String> data, String trace_id) throws IOException {
        return http(path).header(WW.http_header_trace, trace_id).data(data).post();
    }

    public  String post(String path, Map<String, String> data) throws IOException {
        return http(path).data(data).post();
    }

    public  String postBody(String path, byte[] bytes, String contentType) throws IOException {
        return http(path).bodyRaw(new ByteArrayInputStream(bytes), contentType).post();
    }

    public  String postBody(String path, String text, String contentType) throws IOException {
        return http(path).bodyTxt(text, contentType).post();
    }

    public  void postAsync(String path, Map<String, String> data)  {
        try {
            http(path).data(data).postAsync(null);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public  String get(String path) throws IOException {
        return http(path).get();
    }
}
