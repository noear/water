package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.noear.snack.ONode;
import org.noear.water.model.DiscoverTargetM;
import org.noear.water.utils.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;

class RegistryUtils {
    public static String buildMeta(URL url){
        Map<String,String> map = new LinkedHashMap<>(url.getParameters());
        map.put("_path",url.getAbsolutePath());
        map.put("_protocol",url.getProtocol());

        return ONode.stringify(map);
    }

    public static URL buildUrl(DiscoverTargetM target) {
        Map<String, String> meta = ONode.deserialize(target.meta);
        String tmp = meta.get("_protocol") + "://" + target.address + meta.get("_path");

        meta.remove("_protocol");
        meta.remove("_path");

        URL url = URL.valueOf(tmp);
        url.getParameters().putAll(meta);

        return url;
    }

    public static String buildServiceKey(URL url){
        String group = url.getParameter("group", "");
        String service = url.getParameter("interface");

        String service_key = "dubbo:" + service;
        if (TextUtils.isNotEmpty(group)) {
            service_key = service_key + "::" + group;
        }

        return service_key;
    }
}
