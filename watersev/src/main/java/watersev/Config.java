package watersev;

import org.noear.redisx.RedisClient;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsCacheUtils;
import org.noear.wood.DbContext;
import org.noear.wood.cache.ICacheServiceEx;
import org.noear.wood.cache.LocalCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Config {
    //public static final String water_service_name = "watersev";

    public static final ICacheServiceEx cache_file;
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data");
    //public static final ICacheServiceEx cache_data;

    public static final DbContext water;
    public static final DbContext water_paas;

    public static RedisClient rd_track; //db:5

    public static ConfigM water_heihei = cfg(WW.water_heihei);

    public static ConfigM water_log_store = cfg(WW.water_log_store);
    public static ConfigM water_msg_store = cfg(WW.water_msg_store);

    //目前的运行环境（生产环境，预生产环境，测试环境）
    public static String alarm_sign() {
        return cfg("alarm_sign").value;
    }


    static {
        water = DsCacheUtils.getDb(cfg(WW.water).value, true);
        water_paas = DsCacheUtils.getDb(cfg(WW.water_paas).value, true, water);

        cache_file = new LocalCache();
        //cache_data = cfg("water_cache").getCh().nameSet("cache_data"); //2022-10
    }

    public static void tryInit() {
        rd_track = WaterSetting.redis_track_cfg().getRd(5);
    }

    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(WW.water, key);
    }
}
