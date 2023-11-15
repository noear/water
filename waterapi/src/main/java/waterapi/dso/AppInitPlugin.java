package waterapi.dso;

import org.noear.solon.cloud.CloudManager;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.BrokerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.log.CloudLogServiceLocalImp;

import java.util.List;

/**
 * @author noear 2021/11/3 created
 */
public class AppInitPlugin implements Plugin {
    static Logger log = LoggerFactory.getLogger(AppInitPlugin.class);

    @Override
    public void start(AppContext context) {
        //注册本地日志服务
        CloudManager.register(new CloudLogServiceLocalImp());

        //设置消息管道
        try {
            List<BrokerVo> list = DbWaterCfgApi.getBrokerList();
            for (BrokerVo brokerVo : list) {
                ProtocolHub.msgBrokerFactory.getBroker(brokerVo.broker);
            }
        } catch (Throwable e) {
            log.error("Broker init error: {}", e);
        }
    }
}
