package wateradmin;

import org.noear.solon.Solon;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import org.noear.solon.core.NvMap;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import org.noear.water.protocol.solution.MsgBrokerImpl;
import wateradmin.dso.CacheUtil;
import wateradmin.dso.ErrorListener;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.MonitoringAliyun;

public class WateradminApp {
    public static void main(String[] args) {
        NvMap argx = NvMap.from(args);

        System.err.println("[Water] run mode start...");

        Solon.start(WateradminApp.class, argx, x -> {
            Config.tryInit(x);

            x.enableErrorAutoprint(false);
            //x.before(new CrossHandler().allowCredentials(true));


            x.onError(new ErrorListener());

            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);
            ProtocolHub.msgSourceFactory = new MsgBrokerImpl(Config.water_msg_store, CacheUtil.data);

            ProtocolHub.monitoring = new MonitoringAliyun();
        });

        PreheatUtils.preheat("/run/check/");
        PreheatUtils.preheat("/login");
    }
}