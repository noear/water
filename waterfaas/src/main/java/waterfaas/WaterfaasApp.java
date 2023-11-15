package waterfaas;

import luffy.WaterImpl;
import org.noear.rock.RockClient;
import org.noear.rock.RockUtil;
import org.noear.solon.Solon;
import org.noear.luffy.dso.*;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.cloud.utils.http.PreheatUtils;
import org.noear.solon.core.handle.MethodType;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterProxy;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogSourceFactoryImpl;
import luffy.JtRun;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import org.noear.water.track.TrackBuffer;
import waterfaas.controller.AppHandler;
import waterfaas.controller.FrmInterceptor;
import waterfaas.dso.db.DbWaterCfgApi;
import waterfaas.dso.AppInitPlugin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WaterfaasApp {
    public static void main(String[] args) throws Exception{

        //开始
        JtRun.init();

        SolonApp app = Solon.start(WaterfaasApp.class, args, (x) -> {
            //设置接口
            //
            Config.tryInit(x);
            TrackBuffer.singleton().bind(Config.rd_track);

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

            x.sharedAdd("water", WaterImpl.g);

            x.sharedAdd("WaterClient",WaterClient.class);
            x.sharedAdd("WaterProxy", WaterProxy.class);

            x.sharedAdd("LocalDate", LocalDate.class);
            x.sharedAdd("LocalTime", LocalTime.class);
            x.sharedAdd("LocalDateTime", LocalDateTime.class);

            if(Utils.hasClass(()-> RockUtil.class)){
                x.sharedAdd("RockClient", RockClient.class);
                x.sharedAdd("RockUtil", RockUtil.class);
            }

            x.pluginAdd(-1, new AppInitPlugin());
        });

        app.before("**", MethodType.GET, FrmInterceptor.g());
        app.before("**", MethodType.POST, FrmInterceptor.g());

        //文件代理
        app.all("**", AppHandler.g());

        JtRun.xfunInit();

        PreheatUtils.preheat(WW.path_run_check);
    }
}