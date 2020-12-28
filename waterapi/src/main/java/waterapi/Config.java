package waterapi;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.LocalUtils;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.DbUtils;
import waterapi.models.ConfigModel;

import java.util.Properties;

public class Config {
    public static final String water_service_name = "waterapi";

    public static DbContext water;
    public static DbContext water_msg;
    public static DbContext water_log;

    public static RedisX rd_ids;   //db:1
    public static RedisX rd_lock;  //db:2
    public static RedisX rd_msg;   //db:3
    public static RedisX rd_track; //db:5

    public static String water_cache_header;

    public static ConfigM water_msg_queue;

    public static String localHost;



    //================================
    //
    //获取一个数据库配置

    static {
        WeedConfig.isDebug = false;
        WeedConfig.isUsingValueExpression = false;

        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");
    }

    private static boolean _inited = false;

    public static void tryInit() {
        if (_inited == false) {
            _inited = true;

            int service_port = Solon.global().port();;
            Properties prop = Solon.cfg().getProp("water.dataSource");

            water = DbUtils.getDb(prop);
            water_msg = cfg(WW.water_msg).getDb(true);
            water_log = cfg(WW.water_log).getDb(true);

            ConfigModel cm = cfg(WW.water_redis);
            ConfigModel cm2 = cfg(WW.water_redis_track);

            rd_ids = cm.getRd(1);
            rd_lock = cm.getRd(2);
            rd_msg = cm.getRd(3);
            if (cm2 == null || TextUtils.isEmpty(cm2.value)) {
                rd_track = cm.getRd(5);
            } else {
                rd_track = cm2.getRd(5);
            }


            water_cache_header = cfg("water_cache_header")
                    .getString("WATER2_CACHE") + "_API2";

            water_msg_queue = cfg("water_msg_queue").toConfigM();

            try {
                localHost = LocalUtils.getLocalAddr(service_port);

                //本地IP订阅
                //
                DbWaterRegApi.addService(water_service_name,
                        localHost,
                        WW.path_run_check,
                        0,
                        Solon.cfg().isDriftMode());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    //================================
    //

    public static ConfigModel cfg(String key) {
        try {
            return DbWaterCfgApi.getConfigNoCache(WW.water, key);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getLocalHost() {
        return localHost;
    }
}
