package watersev.dso.db;

import org.noear.weed.DbContext;
import watersev.Config;
import watersev.models.water_cfg.LoggerModel;

import java.sql.SQLException;
import java.util.List;

public class DbWaterCfgApi {
    public static DbContext db() {
        return Config.water;
    }

    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles() throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("tag", "_alarm")
                .andEq("type", "mobile")
                .andEq("is_enabled",1)
                .andNeq("value", "")
                .select("value ")
                .caching(Config.cache_data)
                .getArray(0);
    }

    public static boolean hasGateway(String name) {
        try {
            return db().table("water_cfg_properties")
                    .whereEq("tag", "_gateway")
                    .andEq("key", name)
                    .andEq("is_enabled",1)
                    .exists();
        }catch (Exception ex){
            return false;
        }
    }

    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger")
                    .where("logger = ?", logger)
                    .limit(1)
                    .select("*")
                    .getItem(LoggerModel.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
