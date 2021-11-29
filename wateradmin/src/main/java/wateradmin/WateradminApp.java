package wateradmin;

import org.noear.solon.Solon;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import wateradmin.dso.CacheUtil;
import wateradmin.dso.ErrorListener;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.wrap.MonitoringAliyun;

public class WateradminApp {
    public static void main(String[] args) throws Exception {
        Solon.start(WateradminApp.class, args, x -> {
            Config.tryInit(x);

            x.enableErrorAutoprint(false);
            x.onError(new ErrorListener());

            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);
            ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(Config.water_msg_store, CacheUtil.data, DbWaterCfgApi::getBroker);

            ProtocolHub.monitoring = new MonitoringAliyun();
        });

        //注册 gritapi 服务
        Instance instance = new Instance("gritapi", Instance.local().address()).protocol("http");
        CloudClient.discovery().register("grit", instance);

        PreheatUtils.preheat("/run/check/");
        PreheatUtils.preheat("/login");
    }
}