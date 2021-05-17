package org.noear.water.utils;

import org.noear.solon.core.handle.Context;

/**
 * 用户IP获取工具
 *
 * @author noear
 * @since 2.0
 * */
public class IPUtils {
    public static String getIP(Context ctx){
        String ip =  ctx.header("X-Real-IP");

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = ctx.header("X-Forwarded-For");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = ctx.header("Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = ctx.header("WL-Proxy-Client-IP");
        }

        if (TextUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = ctx.ip();
        }

        return ip;
    }
}
