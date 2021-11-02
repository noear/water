package waterraas;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsCacheUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

public class Config {
    public static final String water_service_name = "waterraas";

    public static final String msg_rubber_task = "rubber.task";
    public static final String msg_rubber_notice = "rubber.notice";

    public static final ICacheServiceEx cache_file = new LocalCache().nameSet("cache_file");
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data").nameSet("cache_data");


    public static final DbContext water;
    public static final DbContext water_paas;
    public static final DbContext water_raas;

    static {
        water = DsCacheUtils.getDb(cfg(WW.water).value, true);
        water_paas = DsCacheUtils.getDb(cfg(WW.water_paas).value, true, water);
        water_raas = DsCacheUtils.getDb(cfg(WW.water_raas).value, true, water);
    }

    //向Water注册当前服务
    public static void tryInit() {

    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(WW.water, key);
    }
}
