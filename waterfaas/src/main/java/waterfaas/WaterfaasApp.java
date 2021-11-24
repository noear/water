package waterfaas;

import org.noear.solon.Solon;
import org.noear.luffy.dso.*;
import org.noear.solon.SolonApp;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import org.noear.solon.core.handle.MethodType;
import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import luffy.JtRun;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import waterfaas.controller.AppHandler;
import waterfaas.controller.FrmInterceptor;
import waterfaas.dso.db.DbWaterCfgApi;
import waterfaas.dso.AppInitPlugin;


public class WaterfaasApp {
    public static void main(String[] args) throws Exception{

        //开始
        JtRun.init();

        SolonApp app = Solon.start(WaterfaasApp.class, args, (x) -> {
            Config.tryInit(x);

            x.enableErrorAutoprint(false);

            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;

            //用于清除控制
            ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);
            //用于清除控制
            ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(Config.water_msg_store, Config.cache_data, DbWaterCfgApi::getBroker);


            x.sharedAdd("cache", Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);

            x.pluginAdd(-1, new AppInitPlugin());
        });

        app.before("**", MethodType.GET, FrmInterceptor.g());
        app.before("**", MethodType.POST, FrmInterceptor.g());

        //文件代理
        app.all("**", AppHandler.g());

        JtRun.xfunInit();

        PreheatUtils.preheat("/run/check/");
    }
}