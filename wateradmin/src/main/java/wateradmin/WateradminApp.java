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
import org.noear.water.protocol.solution.IdBuilderImp;
import org.noear.water.protocol.solution.LogQuerierImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.MessageSourceFactoryImp;
import org.noear.water.utils.IDUtils;
import org.noear.weed.DbContext;
import wateradmin.controller.cfg.PropController;
import wateradmin.dso.CacheUtil;
import wateradmin.dso.IDUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.MonitoringAliyun;
import wateradmin.setup.Setup;
import wateradmin.widget.EnumTag;

import javax.sql.DataSource;

public class WateradminApp {
    public static void main(String[] args) {
        //
        // http://139.224.74.31:9371/cfg/get/?tag=water
        //

        NvMap argx = NvMap.from(args);

        if(argx.getInt("setup") == 1){
            setStart(argx);
        }else{
            runStart(argx);
        }
    }

    private static void setStart(NvMap argx) {
        System.err.println("[Water] setup mode start...");

        //添加扩展目录(直接使用 waterapi 的扩展目录)
        argx.set("extend","waterapi_ext");

        SolonApp app = Solon.start(Setup.class, argx, x -> {
            x.beanScan(EnumTag.class);
            x.beanMake(PropController.class);
        });

        Props ps = app.cfg().getProp("water.dataSource");

        if(ps.size() < 4){
            throw new RuntimeException("[Water] Missing water. DataSource configuration");
        }

        ps.setProperty("jdbcUrl", ps.getProperty("url"));

        DataSource ds = Utils.injectProperties(new HikariDataSource(), ps);

        Setup.water = new DbContext(ps.get("schema"), ds);
        Setup.water.initMetaData();

        app.get("/", c -> c.redirect("/cfg/prop"));
    }

    private static void runStart(NvMap argx) {
        System.err.println("[Water] run mode start...");

        WaterLogger logger = new WaterLogger("water_log_admin");

        Solon.start(WateradminApp.class, argx, app -> {
            Config.tryInit(app);


            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;
            ProtocolHub.idBuilder = new IdBuilderImp(Config.water_redis);

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