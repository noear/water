package watersev;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import org.noear.solon.extend.schedule.JobRunner;
import org.noear.luffy.dso.*;
import org.noear.water.WaterClient;
import org.noear.water.protocol.solution.*;
import org.noear.water.protocol.ProtocolHub;
import luffy.JtRun;
import org.noear.water.utils.TextUtils;
import watersev.dso.JobRunnerEx;
import watersev.dso.db.DbWaterCfgApi;

/**
 * 可以按三个服务进行部署：
 *
 * -sss=tool
 * -sss=pln
 * -sss=msg (-pool=n)?
 *
 * */
public class WatersevApp {
    public static void main(String[] args) throws Exception {
        NvMap xMap = NvMap.from(args);

        //是否有端口
        boolean has_server_port = xMap.containsKey("server.port");

        //加载环境变量(支持弹性容器设置的环境)
        String sss = System.getenv("water.sss");

        if (xMap.containsKey("sss")) {
            sss = xMap.get("sss");
        }

        if(TextUtils.isNotEmpty(sss)) {
            xMap.put("sss", sss);
        }

        JobRunner.global = new JobRunnerEx(sss);
        JtRun.init();

        Solon.start(WatersevApp.class, xMap, (x) -> {
            //有端口才开启http能力
            x.enableHttp(has_server_port);
            x.enableErrorAutoprint(false);

            Config.tryInit();

            ProtocolHub.config = WaterClient.Config::get;

            //用于清除控制
            ProtocolHub.logSourceFactory = new LogSourceFactoryImpl(Config.water_log_store, DbWaterCfgApi::getLogger);

            ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(Config.water_msg_store, Config.cache_data);

            ProtocolHub.heihei = new HeiheiImp();

            x.sharedAdd("cache", Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        });

        JtRun.xfunInit();
    }
}
