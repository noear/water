package waterfaas.dso.db;

import org.noear.water.protocol.model.message.BrokerVo;
import org.noear.weed.DbContext;
import waterfaas.Config;
import waterfaas.models.BrokerModel;
import waterfaas.models.LoggerModel;

import java.util.List;

/**
 * @author noear 2021/2/2 created
 */
public class DbWaterCfgApi {
    private static DbContext db() {
        return Config.water;
    }


    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger")
                    .where("logger = ?", logger)
                    .limit(1)
                    .select("*")
                    .getItem(LoggerModel.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BrokerModel getBroker(String broker) {
        try {
            return db().table("water_cfg_broker")
                    .where("broker = ?", broker)
                    .limit(1)
                    .select("*")
                    .getItem(BrokerModel.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static List<BrokerVo> getBrokerList() throws Exception {
        return db().table("water_cfg_broker").whereEq("is_enabled", 1)
                .caching(Config.cache_data)
                .usingCache(10)
                .selectList("*", BrokerVo.class);
    }
}
