package org.noear.water;

import org.noear.solon.cloud.utils.http.HttpUtils;
import org.noear.water.model.LogLevel;
import org.noear.weed.cache.CacheUsing;

import java.util.Map;

/**
 * Water 内部接口代理
 *
 * @author noear
 * @since 2.0
 * */
public class WaterProxy {
    static final String SERVICE_WATER_PAAS = "waterfaas";
    static final String SERVICE_WATER_RAAS = "waterraas";

    public static String job(String service, String name) throws Exception {
        return HttpUtils.http(service, WW.path_run_job).data("name", name).post();
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
        if (path.startsWith("/") == false) {
            path = "/" + path;
        }

        if (args == null || args.size() == 0) {
            return HttpUtils.http(service, path).get();
        } else {
            return HttpUtils.http(service, path).data(args).post();
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
        return HttpUtils.http(url);
    }

    /**
     * 给FaaS用
     * */
    public static void logTrace(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, LogLevel.TRACE, map);
    }

    public static void logDebug(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, LogLevel.DEBUG, map);
    }

    public static void logInfo(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, LogLevel.INFO, map);
    }

    public static void logWarn(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, LogLevel.WARN, map);
    }

    public static void logError(String logger, Map<String, Object> map) {
        WaterClient.Log.append(logger, LogLevel.ERROR, map);
    }
}
