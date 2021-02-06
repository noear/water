package waterpaas;

import org.noear.solon.Solon;
import org.noear.luffy.dso.*;
import org.noear.solon.SolonApp;
import org.noear.solon.core.handle.MethodType;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.log.WaterLogger;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.IdBuilderImp;
import org.noear.water.protocol.solution.LogQuerierImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.MessageSourceFactoryImp;
import org.noear.water.utils.FromUtils;
import org.noear.water.integration.solon.WaterAdapter;
import org.noear.water.utils.Timecount;
import luffy.JtRun;
import waterpaas.controller.AppHandler;
import waterpaas.controller.FrmInterceptor;
import waterpaas.dso.DbWaterCfgApi;


public class WaterpaasApp {
    public static void main(String[] args) {
        JtRun.init();

        SolonApp app = Solon.start(WaterpaasApp.class, args, (x) -> {
            Config.tryInit(x);

            //设置接口
            //
            ProtocolHub.config = WaterClient.Config::get;
            ProtocolHub.idBuilder = new IdBuilderImp(Config.water_redis);

            ProtocolHub.logSourceFactory = new LogSourceFactoryImp(Config.water_log_store, DbWaterCfgApi::getLogger);

            ProtocolHub.messageSourceFactory = new MessageSourceFactoryImp(Config.water_msg_store, Config.cache_data, new WaterLogger(WW.water_log_msg));


            x.sharedAdd("cache", Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        });

        app.before("**", MethodType.GET, FrmInterceptor.g());
        app.before("**", MethodType.POST, FrmInterceptor.g());

        //文件代理
        app.all("**", AppHandler.g());

        JtRun.xfunInit();


        //添加性能记录
        app.before("**", MethodType.HTTP, -1, (c) -> {
            //
            //不记录，检测的性能
            //
            if ("/run/check/".equals(c.path()) == false) {
                c.attrSet("timecount", new Timecount().start());
            }
        });

        app.after("**", MethodType.HTTP, (c) -> {
            Timecount timecount = c.attr("timecount", null);

            if (timecount != null && c.status() != 404) {
                String tag = c.attr("file_tag", "paas");

                long _times = timecount.stop().milliseconds();
                String _node = WaterAdapter.global().localHost();
                String _from = FromUtils.getFrom(c);

                WaterClient.Track.track(WaterAdapter.global().service_name(), tag, c.path(), _times, _node, _from);
            }
        });
    }
}