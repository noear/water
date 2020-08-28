package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class WaterRegistryLib {
    private static Set<URL> cached = new HashSet<>();

    public static void add(URL url) {
        cached.add(url);
    }

    public static void unregister() {
        for (URL url : cached) {
            unregisterDo(url);
        }
    }

    private static void unregisterDo(URL url) {
        if ("consumer".equals(url.getProtocol())) {
            return;
        }

        url = url.removeParameter("timestamp");
        String service = url.getParameter("interface");

        if (TextUtils.isNotEmpty(service)) {
            WaterClient.Registry.unregister(service, url.toFullString());
        }
    }
}
