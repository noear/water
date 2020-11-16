package org.noear.water.solon_plugin;

import org.noear.solon.Solon;

class WaterProps {
    public static String host() {
        return Solon.cfg().get("water.host");
    }

    public static String service_name() {
        return Solon.cfg().get("water.service.name");
    }

    public static String service_tag() {
        return Solon.cfg().get("water.service.tag");
    }

    public static String service_host() {
        return Solon.cfg().get("water.service.host");
    }

    public static String service_alarm() {
        return Solon.cfg().get("water.service.alarm");
    }

    public static String service_secretKey() {
        return Solon.cfg().get("water.service.secretKey");
    }

    public static String service_config() {
        return Solon.cfg().get("water.service.config");
    }
}
