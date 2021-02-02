package wateradmin;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;


public class Config {
    public static String web_title = "WATER";

    public static String water_service_name = "wateradmin";

    public static final DbContext water = cfg(WW.water).getDb(true);
    public static final DbContext water_log = cfg(WW.water_log).getDb(true);
    public static final DbContext water_msg = cfg(WW.water_msg).getDb(true);
    public static final DbContext water_paas = cfg(WW.water_paas).getDb(true);

    public static ConfigM water_log_store = cfg("water_log_store");

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

    public static String waterpaas_secretKey;

    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;
        WeedConfig.isUsingValueExpression = false;
    }

    public static void tryInit(SolonApp app) {

        waterpaas_secretKey = app.cfg().get("waterpaas.secretKey");

        WaterClient.Config.getProperties(WW.water_session).forEach((k, v) -> {
            if (Solon.cfg().isDebugMode()) {
                String key = k.toString();
                if (key.indexOf(".session.") < 0) {
                    Solon.cfg().put(k, v);
                }
            } else {
                Solon.cfg().put(k, v);
            }
        });
    }

    //================================
    //

    public static ConfigM cfg(String key) {
        if (key.indexOf("/") < 0) {
            return WaterClient.Config.get(WW.water, key);
        } else {
            return WaterClient.Config.getByTagKey(key);
        }
    }
}
