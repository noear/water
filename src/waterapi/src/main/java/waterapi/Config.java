package waterapi;

import noear.weed.DbContext;
import noear.weed.WeedConfig;
import waterapi.dao.WaterApi;
import waterapi.dao.db.DbSevApi;
import waterapi.utils.ServerUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yuety on 2017/7/17.
 */
public class Config {

    public static DbContext water = getDbConfig("water");
    public static DbContext water_r = getDbConfig("water_r");
    public static DbContext water_msg = getDbConfig("water_msg");
    public static DbContext water_msg_r = getDbConfig("water_msg_r");
    public static DbContext water_log = getDbConfig("water_log");
    public static DbContext water_log_r = getDbConfig("water_log_r");

    public static boolean is_debug = "1".equals(getUrlConfig("is_debug"));

    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

    private static boolean _inited=false;
    public static void tryInit(HttpServletRequest request) {
        if (_inited == false) {
            _inited = true;

            try {
                DbSevApi.addService("waterapi", ServerUtil.getFullAddress(request), "", "","/run/check/", 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
