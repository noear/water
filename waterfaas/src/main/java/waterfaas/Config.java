package waterfaas;

import org.noear.redisx.RedisClient;
import org.noear.solon.SolonApp;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsCacheUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

public class Config {
    public static final String faas_filter_file = "filter.file";
    public static final String faas_filter_path = "filter.path";
    public static final String faas_hook_start = "hook.start";


    public static final ICacheServiceEx cache_file = new LocalCache();
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data");


    public static final DbContext water ;
    public static final DbContext water_paas ;

    public static RedisClient rd_track; //db:5

    public static ConfigM water_log_store = cfg(WW.water_log_store);
    public static ConfigM water_msg_store = cfg(WW.water_msg_store);

    public static String waterfaas_secretKey;

    static {
        water = DsCacheUtils.getDb(cfg(WW.water).value, true);
        water_paas = DsCacheUtils.getDb(cfg(WW.water_paas).value, true, water);
    }

    public static void tryInit(SolonApp app) {

        //WeedConfig.isUsingValueExpression = false;

        waterfaas_secretKey = app.cfg().get("waterfaas.secretKey");

        rd_track = WaterSetting.redis_track_cfg().getRd(5);
    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(WW.water, key);
    }
}
