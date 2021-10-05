package waterapi;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.Props;
import org.noear.solon.core.handle.Context;
import org.noear.solon.extend.health.HealthHandler;
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

import java.util.Properties;

public class Config {
    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";


    public static final String water_service_name = "waterapi";

    public static DbContext water;
    public static DbContext water_msg;
    public static DbContext water_log;
    public static DbContext water_paas;

    public static RedisX rd_ids;   //db:1
    public static RedisX rd_lock;  //db:2
    public static RedisX rd_msg;   //db:3
    public static RedisX rd_track; //db:5

    public static ConfigM water_redis;

    public static String water_cache_header;

    public static ConfigM water_msg_queue;

    public static String localHost;

    public static ConfigM water_log_store;
    public static ConfigM water_msg_store;


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

            Props prop = Solon.cfg().getProp("water.dataSource");
            if(prop.size() == 0){
                prop = Solon.cfg().getProp("water.ds");
            }

            if (prop.size() > 0) {
                prop.put("driverClassName", "com.mysql.jdbc.Driver");
            }

            String dbServer = prop.getProperty("server");
            String dbSchema = prop.getProperty("schema");
            if (Utils.isNotEmpty(dbServer)) {
                prop.setProperty("url", TML_JDBC_URL.replace(TML_MARK_SERVER, dbServer).replace(TML_MARK_SCHEMA, dbSchema));
            }

            water = DbUtils.getDb(prop);
            water_msg = cfg(WW.water_msg).getDb(true);
            water_log = cfg(WW.water_log).getDb(true);
            water_paas = cfg(WW.water_paas).getDb(true);
            water_redis = cfg(WW.water_redis);

            ConfigM cm2 = cfg(WW.water_redis_track);

            rd_ids = water_redis.getRd(1);
            rd_lock = water_redis.getRd(2);
            rd_msg = water_redis.getRd(3);
            if (cm2 == null || TextUtils.isEmpty(cm2.value)) {
                rd_track = water_redis.getRd(5);
            } else {
                rd_track = cm2.getRd(5);
            }

            water_cache_header = cfg("water_cache_header")
                    .getString("WATER2_CACHE") + "_API2";

            water_log_store = cfg(WW.water_log_store);
            water_msg_store = cfg(WW.water_msg_store);

            water_msg_queue = cfg(WW.water_msg_queue);

            initWeedOnException();
        }
    }

    public static void tryRegService() {
        try {
            localHost = LocalUtils.getLocalAddr(Solon.global().port());
            String code_location = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            //本地IP订阅
            //
            ONode oNode = ONode.load(Solon.cfg().argx());
            oNode.remove("server.port");
            String meta = oNode.toJson();

            DbWaterRegApi.addService(Solon.cfg().appGroup(), water_service_name,
                    localHost, meta, "",
                    HealthHandler.HANDLER_PATH,
                    0,
                    code_location,
                    Solon.cfg().isDriftMode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void initWeedOnException() {
        //
        // 有可能会造成列循环
        //
        WeedConfig.onException((cmd, err) -> {
            if (cmd != null) {
                Context ctx = Context.current();

                if (ctx != null) {
                    //
                    //有可能会造成列循环；所以转给上下文特性
                    //
                    ctx.attrSet("weed_cmd", cmd);
                }
            }
        });
    }


    //================================
    //

    public static ConfigM cfg(String key) {
        try {
            return DbWaterCfgApi.getConfigNoCache(WW.water, key)
                    .toConfigM();
        } catch (Exception ex) {
            throw new RuntimeException("Config loading error: " + key, ex);
        }
    }

    public static String getLocalHost() {
        return localHost;
    }
}
