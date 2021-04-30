package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.water.WaterClient;
import org.noear.water.model.DiscoverTargetM;
import org.noear.water.utils.TaskUtils;
import org.noear.water.utils.TextUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
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
        url = url.removeParameter("pid");

        String service_key = RegistryUtils.buildServiceKey(url);

        if (TextUtils.isNotEmpty(service_key)) {
            String meta = RegistryUtils.buildMeta(url);
            String address = url.getAuthority();

            WaterClient.Registry.unregister(service_key, address, meta);
        }

        System.out.println("unregister!!!");
    }

    public static void register(URL url) {
        if ("consumer".equals(url.getProtocol())) {
            return;
        }

        url = url.removeParameter("timestamp");
        url = url.removeParameter("pid");

        String service_key = RegistryUtils.buildServiceKey(url);

        if (TextUtils.isNotEmpty(service_key)) {
            WaterRegistryLib.add(url);


            String meta = RegistryUtils.buildMeta(url);
            String address = url.getAddress();

            WaterClient.Registry.register(Solon.cfg().appGroup(), service_key, address, meta, Solon.cfg().isDriftMode());
        }
    }
}
