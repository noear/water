package wateradmin;

import org.noear.solon.XApp;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogQuerierImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.LogSourceDef;

public class WateradminApp {
    public static void main(String[] args) {
        //
        // http://139.224.74.31:9371/cfg/get/?tag=water
        //
        XApp.start(WateradminApp.class, args, app -> {
            Config.tryInit();


            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImp(new LogSourceDef(), DbWaterCfgApi::getLogger);
            ProtocolHub.logQuerier = new LogQuerierImp();
        });
    }
}