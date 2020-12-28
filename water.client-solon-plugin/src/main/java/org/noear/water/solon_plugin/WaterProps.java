package org.noear.water.solon_plugin;

import org.noear.solon.Solon;
import org.noear.water.utils.TextUtils;

/**
 * Water 配置属性获取
 *
 * @author noear
 * @since 2.0
 * */
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

    public static String service_hostname() {
        String host = Solon.cfg().get("water.service.hostname");
        if (TextUtils.isEmpty(host)) {
            host = Solon.cfg().get("water.service.host"); //旧的标准
        }

        return host;
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
