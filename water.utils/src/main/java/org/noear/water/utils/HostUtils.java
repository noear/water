package org.noear.water.utils;

/**
 * @author noear 2021/1/22 created
 */
public class HostUtils {
    public static String adjust(String host) {
        if (host.indexOf("://") < 0) {
            host = "http://" + host;
        }

        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 2);
        }

        return host;
    }
}
