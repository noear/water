package org.noear.water;

import org.noear.water.model.ConfigM;
import org.noear.water.utils.HttpUtils;
import org.noear.weed.cache.CacheUsing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Water 内部接口代理
 *
 * @author noear
 * @since 2.0
 * */
public class WaterProxy {
    private static String paas_uri;
    private static String raas_uri;

    private static ConfigM get0(String service) {
        return WaterClient.Config.get("_service", service);
    }

    private static String callDo(String url, String fun, Map<String, Object> args) throws Exception {
        String fun_url = url.replace("{fun}", fun);
        Map<String, String> form = new HashMap();
        if (args != null) {
            args.forEach((k, v) -> {
                if (v != null) {
                    form.put(k, v.toString());
                }

            });
        }

        return http(fun_url).data(form).post();
    }

    /**
     * 调用_service接口
     */
    public static String call(String service, String fun, Map<String, Object> args) throws Exception {
        if (service.indexOf("://") > 0) {
            return callDo(service, fun, args);
        } else {
            ConfigM cfg = get0(service);
            return callDo(cfg.value, fun, args);
        }
    }

    /**
     * 调用_service接口，并尝试缓存控制
     */
    public static String callAndCache(String service, String fun, Map<String, Object> args, CacheUsing cacheUsing) throws Exception {
        if (cacheUsing == null) {
            return call(service, fun, args);
        } else {
            StringBuilder wkey = new StringBuilder();
            wkey.append(service).append("_");
            wkey.append(fun).append("_");
            args.forEach((k, v) -> {
                wkey.append(k).append("_").append(v.toString()).append("_");
            });
            return cacheUsing.getEx(wkey.toString(), () -> call(service, fun, args));
        }
    }

    public final static String callJob(String service, String jobName) throws Exception{
        return WaterLoadBalance.get(service).http(WW.path_run_job).data("name",jobName).post();
    }


    /**
     * 调用RaaS
     */
    public static String raas(String type, String tag, String name, Map<String, Object> args) throws IOException {
        String path = "/" + type + "/" + tag + "/" + name;

        return raas(path, args);
    }

    /**
     * 调用RaaS
     */
    public static String raas(String path, Map<String, Object> args) throws IOException {
        if (raas_uri == null) {
            raas_uri = WaterClient.Config.get("water", "raas_uri").value;
        }

        return http(raas_uri + path).data(args).post();
    }


    /**
     * 调用PaaS
     */
    public final static String paas(String path, Map<String, Object> args) throws IOException {
        if (paas_uri == null) {
            paas_uri = WaterClient.Config.get("water", "paas_uri").value;
        }

        if (args == null) {
            return http(paas_uri + path).get();
        } else {
            return http(paas_uri + path).data(args).post();
        }
    }

    private static HttpUtils http(String url) {
        return HttpUtils.http(url)
                .header(WW.http_header_trace, WaterClient.waterTraceId())
                .header(WW.http_header_from, WaterClient.localServiceHost());
    }
}
