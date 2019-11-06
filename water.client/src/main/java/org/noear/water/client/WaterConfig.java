package org.noear.water.client;

import org.noear.water.tools.TextUtils;

public class WaterConfig {
    public static final String msg_ucache_topic = "water.cache.update";
    public static final String msg_uconfig_topic = "water.config.update";

    public static String water_host = "http://water.zmapi.cn";
    public static final String water_lock = "";

    static {
        String host = System.getProperty("water.host");
        if(TextUtils.isEmpty(host) == false){
            water_host = host;
        }
    }
}
