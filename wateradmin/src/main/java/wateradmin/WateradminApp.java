package wateradmin;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.Props;
import org.noear.solon.extend.cors.CrossHandler;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.MessageSourceFactoryImp;
import org.noear.weed.DbContext;
import wateradmin.controller.BaseController;
import wateradmin.controller.cfg.PropController;
import wateradmin.controller.cfg.WhitelistController;
import wateradmin.dso.CacheUtil;
import wateradmin.dso.ErrorListener;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.MonitoringAliyun;
import wateradmin.setup.Setup;
import wateradmin.widget.EnumTag;

import javax.sql.DataSource;

public class WateradminApp {
    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";

    public static void main(String[] args) {
        NvMap argx = NvMap.from(args);

        //支持环境控制
        //
        String waterSetup = System.getenv("water.setup");
        if (waterSetup != null) {
            argx.set("setup", waterSetup);
        }

        if (argx.getInt("setup") == 1) {
            setStart(argx);
        } else {
            runStart(argx);
        }
    }

    private static void setStart(NvMap argx) {
        System.err.println("[Water] setup mode start...");

        //添加扩展目录(直接使用 waterapi 的扩展目录)
        argx.set("extend", "waterapi_ext");

        SolonApp app = Solon.start(Setup.class, argx, x -> {
            //加载环境变量(支持弹性容器设置的环境)
            x.cfg().loadEnv("water.");

            String s = argx.get("s");
            String u = argx.get("u");
            String p = argx.get("p");

            if (Utils.isNotEmpty(s) && Utils.isNotEmpty(u) && Utils.isNotEmpty(p)) {
                System.setProperty(WW.cfg_water_ds_schema, "water");
                System.setProperty(WW.cfg_water_ds_url, TML_JDBC_URL.replace(TML_MARK_SERVER, s).replace(TML_MARK_SCHEMA, "water"));
                System.setProperty(WW.cfg_water_ds_username, u);
                System.setProperty(WW.cfg_water_ds_password, p);
            }

            x.enableErrorAutoprint(false);

            x.beanScan(EnumTag.class);
            x.beanMake(PropController.class);
            x.beanMake(WhitelistController.class);
        });

        System.setProperty(WW.cfg_water_ds_driverClassName, "com.mysql.jdbc.Driver");

        //构建数据源
        Props prop = app.cfg().getProp("water.dataSource");
        if(prop.size() == 0){
            prop = app.cfg().getProp("water.ds");
        }

        if (prop.size() < 4) {
            throw new RuntimeException("[Water] Missing water. DataSource configuration");
        }
        String dbServer = prop.getProperty("server");
        String dbSchema = prop.getProperty("schema");
        if (Utils.isNotEmpty(dbServer)) {
            prop.setProperty("url", TML_JDBC_URL.replace(TML_MARK_SERVER, dbServer).replace(TML_MARK_SCHEMA, dbSchema));
        }
        prop.setProperty("jdbcUrl", prop.getProperty("url"));

        DataSource ds = Utils.injectProperties(new HikariDataSource(), prop);

        Setup.water = new DbContext(dbSchema, ds);
        Setup.water.initMetaData();

        //添加设置监听
        app.get("/", c -> c.render(new BaseController().view("setup")));

        System.out.println("[Water] setup open http://localhost:" + app.port() + "/");
    }

    private static void runStart(NvMap argx) {
        System.err.println("[Water] run mode start...");

        Solon.start(WateradminApp.class, argx, x -> {
            Config.tryInit(x);

            x.enableErrorAutoprint(false);
            //x.before(new CrossHandler().allowCredentials(true));


            x.onError(new ErrorListener());

            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImp(Config.water_log_store, DbWaterCfgApi::getLogger);
            ProtocolHub.messageSourceFactory = new MessageSourceFactoryImp(Config.water_msg_store, CacheUtil.data);

            ProtocolHub.monitoring = new MonitoringAliyun();
        });

        PreheatUtils.preheat("/run/check/");
        PreheatUtils.preheat("/login");
    }
}