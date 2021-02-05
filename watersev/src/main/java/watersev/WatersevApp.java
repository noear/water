package watersev;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import org.noear.solon.extend.schedule.JobFactory;
import org.noear.solon.extend.schedule.JobRunner;
import org.noear.luffy.dso.*;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;
import org.noear.water.log.WaterLogger;
import org.noear.water.protocol.solution.HeiheiImp;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.LogQuerierImp;
import org.noear.water.protocol.solution.LogSourceFactoryImp;
import org.noear.water.protocol.solution.MessageLockRedis;
import luffy.JtRun;
import watersev.dso.IDUtil;
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


        JobRunner.global = new JobRunnerEx(xMap.get("sss"));
        JtRun.init();

        Solon.start(WatersevApp.class, xMap, (x) -> {
            //有端口才开启http能力
            x.enableHttp(has_server_port);

            Config.tryInit();

            ProtocolHub.config = WaterClient.Config::get;
            ProtocolHub.idBuilder = IDUtil::getID;

            ProtocolHub.logSourceFactory = new LogSourceFactoryImp(Config.water_log_store, DbWaterCfgApi::getLogger);


            ProtocolHub.messageLock = new MessageLockRedis(Config.rd_lock);
            ProtocolHub.messageQueue = ProtocolHub.getMessageQueue(Config.water_msg_queue);
            ProtocolHub.heihei = new HeiheiImp(new WaterLogger());

            x.sharedAdd("cache", Config.cache_data);
            x.sharedAdd("XFun", JtFun.g);
            x.sharedAdd("XMsg", JtMsg.g);
            x.sharedAdd("XUtil", JtUtil.g);
            x.sharedAdd("XLock", JtLock.g);
        }).onError((ex) -> {
            WaterClient.Log.append("water_log_sev", Level.ERROR,"global", "", ex);
        });

        JtRun.xfunInit();
    }
}
