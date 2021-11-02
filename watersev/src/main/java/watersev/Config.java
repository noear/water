package watersev;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsCacheUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Config {
    public static final String water_service_name = "watersev";

    public static final ICacheServiceEx cache_file = new LocalCache();
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data");

    public static final DbContext water;
    public static final DbContext water_paas;

    public static ConfigM water_log_store = cfg(WW.water_log_store);
    public static ConfigM water_msg_store = cfg(WW.water_msg_store);

    //目前的运行环境（生产环境，预生产环境，测试环境）
    public static String alarm_sign() {
        return cfg("alarm_sign").value;
    }


    public static ExecutorService pools = Executors.newCachedThreadPool();

    static {
        water = DsCacheUtils.getDb(cfg(WW.water).value, true);
        water_paas = DsCacheUtils.getDb(cfg(WW.water_paas).value, true, water);
    }

    public static void tryInit() {

    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(WW.water, key);
    }
}
