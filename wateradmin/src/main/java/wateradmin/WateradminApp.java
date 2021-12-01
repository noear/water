package wateradmin;

import org.noear.solon.Solon;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import org.noear.solon.core.event.EventBus;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import wateradmin.dso.CacheUtil;
import wateradmin.dso.ErrorListener;
import wateradmin.dso.InitPlugin;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.MonitoringAliyun;

public class WateradminApp {
    public static void main(String[] args) throws Exception {
        Solon.start(WateradminApp.class, args, x -> {
            Config.tryInit(x);

            x.enableErrorAutoprint(false);
            x.onError(new ErrorListener());
            x.pluginAdd(0, new InitPlugin());

            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);
            ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(Config.water_msg_store, CacheUtil.data, DbWaterCfgApi::getBroker);

            ProtocolHub.monitoring = new MonitoringAliyun();
        });

        //尝试注册 gritapi 服务
        gritApiRegTry();

        PreheatUtils.preheat("/run/check/");
        PreheatUtils.preheat("/login");
    }

    /**
     * 尝试注册 gritapi 服务
     * */
    private static void gritApiRegTry() {
        try {
            if (CloudClient.discovery() != null) {
                Instance instance = new Instance("gritapi", Instance.local().address()).protocol("http");
                CloudClient.discovery().register("grit", instance);
            }
        } catch (Exception e) {
            EventBus.push(e);
        }
    }
}