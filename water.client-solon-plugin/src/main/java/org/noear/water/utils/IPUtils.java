package org.noear.water.utils;

import org.noear.solon.core.handle.Context;

/**
 * 用户IP获取工具
 *
 * @author noear
 * @since 2.0
 * */
public class IPUtils {
    public static String getIP(Context request){
        String ip =  request.header("RemoteIp");

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("X-Forwarded-For");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("WL-Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.ip();
        }

        return ip;
    }
}
