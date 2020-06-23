package waterapp;

import org.noear.solon.XUtil;
import org.noear.water.utils.LocalUtils;
import org.noear.water.utils.RedisX;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import waterapp.dso.DbUtils;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.dso.db.DbWaterRegApi;
import waterapp.models.ConfigModel;

import java.util.Properties;

public class Config {
    public static final String water_service_name = "waterapi";
    public static final String water_config_tag = "water";

    public static DbContext water;
    public static DbContext water_msg;
    public static DbContext water_log;

    public static RedisX rd_ids;   //db:1
    public static RedisX rd_lock;  //db:2
    public static RedisX rd_msg;   //db:3
    public static RedisX rd_track; //db:5

    public static boolean is_debug;

    public static String water_cache_header;

    public static Properties water_msg_queue;

    public static String localHost;



    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;
        WeedConfig.isUsingValueExpression = false;

        XUtil.loadClass("com.mysql.jdbc.Driver");
        XUtil.loadClass("com.mysql.cj.jdbc.Driver");
    }

    private static boolean _inited = false;

    public static void tryInit(int service_port, Properties prop) {
        if (_inited == false) {
            _inited = true;

            water = DbUtils.getDb(prop);
            water_msg = cfg("water_msg").getDb(true);
            water_log = cfg("water_log").getDb(true);

            ConfigModel cm = cfg("water_redis");

            rd_ids   = cm.getRd(1);
            rd_lock  = cm.getRd(2);
            rd_msg   = cm.getRd(3);
            rd_track = cm.getRd(5);

            is_debug = "1".equals(cfg("is_debug").getString());

            water_cache_header = cfg("water_cache_header")
                                 .getString( "WATER2_CACHE") + "_API2";

            water_msg_queue = cfg("water_msg_queue").getProp();

            try {
                localHost = LocalUtils.getLocalAddr(service_port);
                DbWaterRegApi.addService(water_service_name,
                        localHost,
                        "/run/check/",
                        0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    //================================
    //

    public static ConfigModel cfg(String key) {
        try {
            return DbWaterCfgApi.getConfigNoCache(water_config_tag, key);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
