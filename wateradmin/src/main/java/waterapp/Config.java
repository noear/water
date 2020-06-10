package waterapp;

import org.noear.solon.XApp;
import org.noear.solon.XUtil;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;

public class Config {
    public static String web_title = "WATER";

    public static String water_service_name = "wateradmin";
    public static String water_config_tag = "water";

    public static DbContext water = cfg("water").getDb(true);
    public static DbContext water_log = cfg("water_log").getDb(true);
    public static DbContext water_msg = cfg("water_msg").getDb(true);

    //paas 根地址
    public static String paas_uri(){
        return cfg("paas_uri").getString();
    }

    //raas 根地址
    public static String raas_uri(){
        return cfg("raas_uri").getString();
    }

    //是否使用标答检查器？
    public static boolean is_use_tag_checker(){
        return "1".equals(cfg("is_use_tag_checker").getString());
    }

    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;
        WeedConfig.isUsingValueExpression = false;
    }

    public static void tryInit() {

        WaterClient.Config.getProperties("water_session").forEach((k, v) -> {
            if (XApp.cfg().isDebugMode()) {
                String key = k.toString();
                if (key.indexOf(".session.") < 0) {
                    XApp.cfg().put(k, v);
                }
            } else {
                XApp.cfg().put(k, v);
            }
        });
    }

    //================================
    //

    public static ConfigM cfg(String key) {
        if (key.indexOf("/") < 0) {
            return WaterClient.Config.get(water_config_tag, key);
        } else {
            return WaterClient.Config.getByTagKey(key);
        }
    }
}
