package org.noear.water;

import org.noear.water.log.Level;
import org.noear.water.log.WaterLogger;
import org.noear.water.utils.HttpUtils;
import org.noear.weed.cache.CacheUsing;

import java.util.Map;

/**
 * Water 内部接口代理
 *
 * @author noear
 * @since 2.0
 * */
public class WaterProxy {
    static final String SERVICE_WATER_PAAS = "waterpaas";
    static final String SERVICE_WATER_RAAS = "waterraas";

    public static String job(String service, String name) throws Exception {
        return WaterUpstream.get(service).http(WW.path_run_job).data("name", name).post();
    }

    /**
     * 调用_service接口，并尝试缓存控制
     */
    public static String call(String service, String path) throws Exception {
        return call(service, path, null);
    }

    /**
     * 调用_service接口，并尝试缓存控制
     */
    public static String call(String service, String path, Map<String, Object> args) throws Exception {
        if (args == null || args.size() == 0) {
            return WaterUpstream.get(service).http(path).get();
        } else {
            return WaterUpstream.get(service).http(path).data(args).post();
        }
    }

    /**
     * 调用_service接口，并尝试缓存控制
     */
    public static String callAndCache(String service, String path, Map<String, Object> args, CacheUsing cacheUsing) throws Exception {
        if (cacheUsing == null) {
            return call(service, path, args);
        } else {
            StringBuilder cacheKey = new StringBuilder();
            cacheKey.append(service).append("_");
            cacheKey.append(path).append("_");
            args.forEach((k, v) -> {
                cacheKey.append(k).append("_").append(v.toString()).append("_");
            });
            return cacheUsing.getEx(cacheKey.toString(), () -> call(service, path, args));
        }
    }

    /**
     * 调用RaaS（旧写法）
     */
    public static String raas(String type, String tag, String name, Map<String, Object> args) throws Exception {
        String path = "/" + type + "/" + tag + "/" + name;

        return raas(path, args);
    }

    /**
     * 调用RaaS（新写法）
     */
    public static String raas(String path, Map<String, Object> args) throws Exception {
        return call(SERVICE_WATER_RAAS, path, args);
    }


    /**
     * 调用FaaS
     */
    public final static String faas(String path) throws Exception {
        return faas(path, null);
    }

    public final static String faas(String path, Map<String, Object> args) throws Exception {
        return call(SERVICE_WATER_PAAS, path, args);
    }

    /**
     * 更名为FaaS
     */
    @Deprecated
    public final static String paas(String path, Map<String, Object> args) throws Exception {
        return faas(path, args);
    }

    private static HttpUtils http(String url) {
        return HttpUtils.http(url)
                .header(WW.http_header_trace, WaterClient.waterTraceId())
                .header(WW.http_header_from, WaterClient.localServiceHost());
    }

    /**
     * 给FaaS用
     * */
    public static void logTrace(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, Level.TRACE, map);
    }

    public static void logDebug(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, Level.DEBUG, map);
    }

    public static void logInfo(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, Level.INFO, map);
    }

    public static void logWarn(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, Level.WARN, map);
    }

    public static void logError(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, Level.ERROR, map);
    }
}
