package watersev;

import noear.weed.DbContext;
import noear.weed.cache.ICacheService;
import watersev.dao.WaterApi;

/**
 * Created by yuety on 2017/7/18.
 */
public class Config {
    public static DbContext water = getDbConfig("water");
    public static DbContext water_r = getDbConfig("water_r");
    public static DbContext water_msg = getDbConfig("water_msg");
    public static DbContext water_msg_r = getDbConfig("water_msg_r");

    //目前的运行环境（生产环境，预生产环境，测试环境）
    public static String environment = getUrlConfig("env");


    //================================
    //
    //获取一个数据库配置
    public static DbContext getDbConfig(String key) {
        WaterApi.ConfigModel cfg = WaterApi.Config.get(key);

        if (cfg == null) {
            return null;
        } else {
            return new DbContext(cfg.key.replace("_r", ""), cfg.url, cfg.user, cfg.password, "");
        }
    }


    public static String getUrlConfig(String key) {
        WaterApi.ConfigModel cfg = WaterApi.Config.get(key);

        if (cfg == null) {
            return null;
        } else {
            return cfg.url;
        }
    }
}
