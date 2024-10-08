package watersev.dso.db;

import org.noear.water.protocol.model.message.BrokerVo;
import org.noear.wood.DbContext;
import watersev.Config;
import watersev.models.water_cfg.LoggerModel;

import java.sql.SQLException;
import java.util.List;

public class DbWaterCfgApi {
    public static DbContext db() {
        return Config.water;
    }

    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles(String tag) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("tag", tag)
                .andEq("type", "mobile")
                .andEq("is_enabled", 1)
                .andNeq("value", "")
                .caching(Config.cache_data)
                .selectArray("value ");
    }

    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger")
                    .where("logger = ?", logger)
                    .limit(1)
                    .selectItem("*", LoggerModel.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BrokerVo getBroker(String broker) {
        try {
            return db().table("water_cfg_broker")
                    .where("broker = ?", broker)
                    .limit(1)
                    .selectItem("*", BrokerVo.class);
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
