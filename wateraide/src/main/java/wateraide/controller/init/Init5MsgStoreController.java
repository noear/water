package wateraide.controller.init;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.solution.MsgBrokerFactoryImpl;
import wateraide.Config;
import wateraide.dso.db.DbWaterCfgApi;
import wateraide.models.view.water_cfg.BrokerModel;

import java.util.List;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class Init5MsgStoreController {

    @Post
    @Mapping("/ajax/init/water_msg")
    public Result ajax_connect(String config)  {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        if (Utils.isEmpty(config)) {
            return Result.failure("出错，配置不能为空");
        }


        ProtocolHub.config = Config::getCfg;

        ConfigM msgCfg = new ConfigM("water_msg_store", config, 0);
        ProtocolHub.msgBrokerFactory = new MsgBrokerFactoryImpl(msgCfg, null, DbWaterCfgApi::getBroker);

        List<BrokerModel> brokerList = DbWaterCfgApi.getBrokerList();

        try {
            for (BrokerModel broker : brokerList) {
                if (Utils.isEmpty(broker.source)) {
                    ProtocolHub.getMsgSource(broker.broker)
                            .create();
                }
            }

            //更新配置
            DbWaterCfgApi.updConfig(WW.water, WW.water_msg_store, config);
            DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "5");
        }catch (Exception e){
            EventBus.push(e);
            return Result.failure("出错，" + e.getLocalizedMessage());
        }

        //2.
        return Result.succeed(null, "配置成功");
    }
}
