package waterapp;

import org.noear.solon.XUtil;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

public class Config {
    public static final String faas_filter_file = "filter.file";
    public static final String faas_filter_path = "filter.path";
    public static final String faas_hook_start  = "hook.start";

    public static final String water_config_tag = "water";

    public static final ICacheServiceEx cache_file = new LocalCache();
    public static final ICacheServiceEx cache_data = new LocalCache().nameSet("cache_data");

    public static final DbContext water_paas = cfg("water_paas").getDb(true);

    public static void tryInit() {

        //WeedConfig.isUsingValueExpression = false;
    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(water_config_tag,key);
    }
}
