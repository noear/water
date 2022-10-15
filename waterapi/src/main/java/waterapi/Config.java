package waterapi;

import org.noear.redisx.RedisClient;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.Props;
import org.noear.solon.core.handle.Context;
import org.noear.solon.health.HealthHandler;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsCacheUtils;
import org.noear.water.utils.DsUtils;
import org.noear.water.utils.LocalUtils;
import org.noear.water.utils.TextUtils;
import org.noear.wood.DbContext;
import org.noear.wood.WoodConfig;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.db.DbWaterRegApi;

public class Config {
    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";


    public static final String water_service_name = "waterapi";

    public static DbContext water;
    public static DbContext water_paas;

    public static RedisClient rd_ids;   //db:1
    public static RedisClient rd_lock;  //db:2
    public static RedisClient rd_msg;   //db:3
    public static RedisClient rd_track; //db:5

    public static ConfigM water_redis;
    public static ConfigM water_heihei;

    public static String localHost;

    public static ConfigM water_log_store;
    public static ConfigM water_msg_store;


    //================================
    //
    //获取一个数据库配置

    static {
        WoodConfig.isDebug = false;
        WoodConfig.isUsingValueExpression = false;

        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");
    }

    private static boolean _inited = false;

    public static void tryInit() {
        if (_inited == false) {
            _inited = true;

            Props props = Solon.cfg().getProp("water.dataSource");
            if (props.size() < 3) {
                props = Solon.cfg().getProp("water.ds");
            }

            String dbServer = props.getProperty("server");
            String dbSchema = props.getProperty("schema");

            if (Utils.isEmpty(dbSchema)) {
                dbSchema = "water";
                props.setProperty("schema", dbSchema);
            }

            if (Utils.isNotEmpty(dbServer)) {
                props.setProperty("url", TML_JDBC_URL.replace(TML_MARK_SERVER, dbServer).replace(TML_MARK_SCHEMA, dbSchema));
            }

            String propsJson = ONode.stringify(props);
            if (props.size() < 3) {
                throw new IllegalArgumentException("Water db configuration error: " + propsJson);
            }

            //必须最先被初始化
            System.out.println("[Water] start config water db by: " + propsJson);
            water = DsUtils.getDb(props, true);
            System.out.println("[Water] start config water paas db...");
            water_paas = DsCacheUtils.getDb(cfg(WW.water_paas).value, true, water);

            System.out.println("[Water] start config water redis...");
            water_redis = cfg(WW.water_redis);

            rd_ids = water_redis.getRd(1);
            rd_lock = water_redis.getRd(2);
            rd_msg = water_redis.getRd(3);

            ConfigM cm2 = cfg(WW.water_redis_track);
            if (cm2 == null || TextUtils.isEmpty(cm2.value)) {
                rd_track = water_redis.getRd(5);
            } else {
                rd_track = cm2.getRd(5);
            }

            water_log_store = cfg(WW.water_log_store);
            water_msg_store = cfg(WW.water_msg_store);

            water_heihei = cfg(WW.water_heihei);

            initWoodOnException();

            System.out.println("[Water] config completed.");
        }
    }

    public static void tryRegService() {
        try {
            localHost = LocalUtils.getLocalAddr(Solon.cfg().serverPort());
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

    private static void initWoodOnException() {
        //
        // 有可能会造成列循环
        //
        WoodConfig.onException((cmd, err) -> {
            if (cmd != null) {
                Context ctx = Context.current();

                if (ctx != null) {
                    //
                    //有可能会造成列循环；所以转给上下文特性
                    //
                    ctx.attrSet("wood_cmd", cmd);
                }
            }
        });
    }


    //================================
    //

    public static ConfigM cfg(String key) {
        return cfg(WW.water, key);
    }

    public static ConfigM cfg(String tag, String key) {
        try {
            return DbWaterCfgApi.getConfigNoCache(tag, key)
                    .toConfigM();
        } catch (Exception ex) {
            throw new RuntimeException("Config loading error: " + key, ex);
        }
    }

    public static String getLocalHost() {
        return localHost;
    }
}
