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
}
