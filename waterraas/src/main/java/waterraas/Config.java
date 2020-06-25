package waterraas;

import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

public class Config {
    public static final String water_service_name = "waterraas";
    public static final String water_config_tag = "water";

    public static final String msg_rubber_task = "rubber.task";
    public static final String msg_rubber_notice = "rubber.notice";

    public static String rubber_uri = cfg("raas_uri").value;

    public static final ICacheServiceEx cache_file = new LocalCache().nameSet("cache_file");
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data").nameSet("cache_data");

    public static final DbContext water = cfg("water").getDb(true);
    public static final DbContext water_paas = cfg("water_paas").getDb(true);

    //向Water注册当前服务
    public static void tryInit(){

    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(water_config_tag,key);
    }
}
