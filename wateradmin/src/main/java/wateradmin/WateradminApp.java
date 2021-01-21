package wateradmin;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import org.noear.water.log.WaterLogger;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogQuerierImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.LogSourceDef;
import wateradmin.dso.wrap.MonitoringAliyun;

public class WateradminApp {
    public static void main(String[] args) {
        //
        // http://139.224.74.31:9371/cfg/get/?tag=water
        //

        WaterLogger logger = new WaterLogger("water_log_admin");

        Solon.start(WateradminApp.class, args, app -> {
            Config.tryInit(app);


            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImp(new LogSourceDef(), DbWaterCfgApi::getLogger);
            ProtocolHub.logQuerier = new LogQuerierImp();

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