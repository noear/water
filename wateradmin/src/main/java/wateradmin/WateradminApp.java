package wateradmin;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.Props;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.log.WaterLogger;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.MessageSourceFactoryImp;
import org.noear.weed.DbContext;
import wateradmin.controller.BaseController;
import wateradmin.controller.cfg.PropController;
import wateradmin.controller.cfg.WhitelistController;
import wateradmin.dso.CacheUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.MonitoringAliyun;
import wateradmin.setup.Setup;
import wateradmin.widget.EnumTag;

import javax.sql.DataSource;

public class WateradminApp {
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
                System.setProperty(WW.cfg_water_ds_url, "jdbc:mysql://" + s + "/water?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
                System.setProperty(WW.cfg_water_ds_username, u);
                System.setProperty(WW.cfg_water_ds_password, p);
            }

            x.beanScan(EnumTag.class);
            x.beanMake(PropController.class);
            x.beanMake(WhitelistController.class);
        });

        System.setProperty(WW.cfg_water_ds_driverClassName, "com.mysql.jdbc.Driver");

        Props ps = app.cfg().getProp("water.dataSource");

        if (ps.size() < 4) {
            throw new RuntimeException("[Water] Missing water. DataSource configuration");
        }

        ps.setProperty("jdbcUrl", ps.getProperty("url"));

        DataSource ds = Utils.injectProperties(new HikariDataSource(), ps);

        Setup.water = new DbContext(ps.get("schema"), ds);
        Setup.water.initMetaData();

        app.get("/", c -> c.render(new BaseController().view("setup")));

        System.out.println("[Water] setup open http://localhost:" + app.port() + "/");
    }

    private static void runStart(NvMap argx) {
        System.err.println("[Water] run mode start...");

        WaterLogger logger = new WaterLogger("water_log_admin");

        Solon.start(WateradminApp.class, argx, app -> {
            Config.tryInit(app);


            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImp(Config.water_log_store, DbWaterCfgApi::getLogger);
            ProtocolHub.messageSourceFactory = new MessageSourceFactoryImp(Config.water_msg_store, CacheUtil.data, new WaterLogger(WW.water_log_msg));

            ProtocolHub.monitoring = new MonitoringAliyun();
        }).onError((ex) -> {
            Context ctx = Context.current();

            if (ctx == null) {
                logger.error("global", "", ex);
            } else {
                String summary = ONode.stringify(ctx.paramMap());
                logger.error(ctx.path(), summary, ex);
            }
        });
    }
}