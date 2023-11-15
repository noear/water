package watersev.dso;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.water.config.ServerConfig;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.BrokerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watersev.dso.db.DbWaterCfgApi;
import watersev.dso.db.DbWaterCfgSafeApi;

import java.util.List;

/**
 * @author noear 2021/11/3 created
 */
public class InitPlugin implements Plugin {
    static Logger log = LoggerFactory.getLogger(InitPlugin.class);

    @Override
    public void start(AppContext context) {
        try {
            List<BrokerVo> list = DbWaterCfgApi.getBrokerList();
            for (BrokerVo brokerVo : list) {
                ProtocolHub.msgBrokerFactory.getBroker(brokerVo.broker);
            }
        } catch (Throwable e) {
            log.error("Broker init error: {}", e);
        }

        try {
            ServerConfig.taskToken = DbWaterCfgSafeApi.getServerTokenOne();
        } catch (Throwable e) {
            log.error("ServerConfig.taskToken init error: {}", e);
        }
    }
}
