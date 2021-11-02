package watersetup.controller.init;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import watersetup.Config;
import watersetup.dso.db.DbWaterCfgApi;
import watersetup.models.water_cfg.BrokerModel;

import java.util.List;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class WaterMsgStoreController {

    @Post
    @Mapping("/ajax/init/water_msg")
    public Result ajax_connect(String config) throws Exception {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }


        ProtocolHub.config = Config::getCfg;

        ConfigM msgCfg = new ConfigM("water_msg_store", config, 0);
        ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(msgCfg, null, DbWaterCfgApi::getBroker);

        List<BrokerModel> brokerList = DbWaterCfgApi.getBrokerList();

        for(BrokerModel broker : brokerList) {
            if (Utils.isEmpty(broker.source)) {
                ProtocolHub.getMsgSource(broker.broker)
                        .create();
            }
        }

        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "5");

        //2.
        return Result.succeed(null, "配置成功");
    }
}
