package watersev;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Config {
    public static final String water_service_name = "watersev";

    public static final ICacheServiceEx cache_file = new LocalCache();
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data");

    public static final DbContext water = cfg(WW.water).getDb(true);
    public static final DbContext water_msg = cfg(WW.water_msg).getDb(true);
    public static final DbContext water_paas = cfg(WW.water_paas).getDb(true);

    public static RedisX rd_ids = cfg(WW.water_redis).getRd(1);
    public static RedisX rd_lock = cfg(WW.water_redis).getRd(2);
    public static RedisX rd_msg = cfg(WW.water_redis).getRd(3);
    public static RedisX rd_count = cfg(WW.water_redis).getRd(6);

    public static ConfigM water_redis = cfg(WW.water_redis);

    public static ConfigM water_msg_queue = cfg("water_msg_queue");
    public static ConfigM water_log_store = cfg("water_log_store");

    //目前的运行环境（生产环境，预生产环境，测试环境）
    public static String alarm_sign() {
        return cfg("alarm_sign").value;
    }


    public static ExecutorService pools = Executors.newCachedThreadPool();

    public static void tryInit() {

    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(WW.water, key);
    }
}
