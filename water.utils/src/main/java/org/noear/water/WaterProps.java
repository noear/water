package org.noear.water;

import org.noear.water.utils.TextUtils;

/**
 * Water 配置属性获取
 *
 * @author noear
 * @since 2.0
 * */
public class WaterProps {
    public static String host() {
        return System.getProperty("water.host");
    }

    public static String service_name() {
        String name = System.getProperty("app.name");

        if(TextUtils.isEmpty(name)){
            name = System.getProperty("solon.app.name");
        }

        if(TextUtils.isEmpty(name)){
            name = System.getProperty("water.service.name");
        }

        return name;
    }

    public static String service_tag() {
        return System.getProperty("water.service.tag");
    }

    public static String service_hostname() {
        String host = System.getProperty("water.service.hostname");
        if (TextUtils.isEmpty(host)) {
            host = System.getProperty("water.service.host"); //旧的标准
        }

        return host;
    }

    public static String service_alarm() {
        return System.getProperty("water.service.alarm");
    }

    public static String service_secretKey() {
        return System.getProperty("water.service.secretKey");
    }

    public static String service_config() {
        return System.getProperty("water.service.config");
    }
}
