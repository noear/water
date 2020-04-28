package org.noear.water.solon_plugin;

import org.noear.solon.XApp;

class WaterProps {
    public static String host() {
        return XApp.cfg().get("water.host");
    }

    public static String service_name() {
        return XApp.cfg().get("water.service.name");
    }

    public static String service_host() {
        return XApp.cfg().get("water.service.host");
    }

    public static String service_alarm() {
        return XApp.cfg().get("water.service.alarm");
    }

    public static String service_secretKey() {
        return XApp.cfg().get("water.service.secretKey");
    }

    public static String service_config() {
        return XApp.cfg().get("water.service.config");
    }
}
