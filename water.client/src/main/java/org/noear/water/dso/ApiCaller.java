package org.noear.water.dso;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.LoadBalanceM;
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
class ApiCaller {
    private LoadBalanceM urlLb;
    private boolean asLongHttp;

    public ApiCaller(LoadBalanceM serverLb) {
        urlLb = serverLb;
        asLongHttp = false;
    }

    public ApiCaller asLongHttp() {
        asLongHttp = true;
        return this;
    }

    public ApiCaller asShortHttp() {
        asLongHttp = false;
        return this;
    }

    public HttpUtils http(String path) {
        String url;
        String server = urlLb.get();

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

        HttpUtils httpUtils = HttpUtils.http(url);

        if (asLongHttp) {
            httpUtils.asLongHttp();
        } else {
            httpUtils.asShortHttp();
        }

        return httpUtils.header(WW.http_header_token, WaterClient.waterAclToken())
                .header(WW.http_header_version, WW.water_version)
                .header(WW.http_header_trace, WaterClient.waterTraceId())
                .header(WW.http_header_from, WaterClient.localServiceHost());
    }

    public String post(String path, Map<String, String> data, String trace_id) throws IOException {
        return http(path).header(WW.http_header_trace, trace_id).data(data).post();
    }

    public String post(String path, Map<String, String> data) throws IOException {
        return http(path).data(data).post();
    }

    public String postBody(String path, byte[] bytes, String contentType) throws IOException {
        return http(path).bodyRaw(new ByteArrayInputStream(bytes), contentType).post();
    }

    public String postBody(String path, String text, String contentType) throws IOException {
        return http(path).bodyTxt(text, contentType).post();
    }

    public void postAsync(String path, Map<String, String> data) {
        try {
            http(path).data(data).postAsync(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String get(String path) throws IOException {
        return http(path).get();
    }
}
