package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.noear.solon.XApp;
import org.noear.water.WaterClient;
import org.noear.water.utils.TaskUtils;
import org.noear.water.utils.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class WaterRegistryLib {
    private static Set<URL> cached = new HashSet<>();
    private static boolean is_closed = false;

    public static void add(URL url) {
        cached.add(url);
    }

    public static void stop() {
        is_closed = true;

//        for (URL url : cached) {
//            unregisterDo(url);
//        }
    }

    public static void start() {
        TaskUtils.run(1000 * 4, () -> {
            if (is_closed) {
                return;
            }

            for (URL url : cached) {
                register(url);
            }
        });
    }

    public static void unregister(URL url) {
        if ("consumer".equals(url.getProtocol())) {
            return;
        }

        url = url.removeParameter("timestamp");
        String group = url.getParameter("group","");
        String service = url.getParameter("interface");

        if (TextUtils.isNotEmpty(service)) {

            WaterClient.Registry.unregister(group + ":" + service, url.toFullString());
        }

        System.out.println("unregister!!!");
    }

    public static void register(URL url) {
        if ("consumer".equals(url.getProtocol())) {
            return;
        }

        url = url.removeParameter("timestamp");
        String group = url.getParameter("group","");
        String service = url.getParameter("interface");

        if (TextUtils.isNotEmpty(service)) {
            WaterRegistryLib.add(url);

            WaterClient.Registry.register(group + ":" + service, url.toFullString(), XApp.cfg().isDriftMode());
        }
    }
}
