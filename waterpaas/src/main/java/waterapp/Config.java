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
    public static final ICacheServiceEx cache_data = new LocalCache();

    public static DbContext water = cfg("water").getDb(true);

    public static void tryInit() {

        XUtil.loadClass("com.mysql.jdbc.Driver");
        XUtil.loadClass("com.mysql.cj.jdbc.Driver");

        //WeedConfig.isUsingValueExpression = false;
    }


    public static ConfigM cfg(String key) {
        return WaterClient.Config.get(water_config_tag,key);
    }
}
