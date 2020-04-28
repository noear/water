package waterapp.dso.db;

import waterapp.Config;

import java.sql.SQLException;
import java.util.List;

public class DbWaterCfgApi {
    //获取账号的手机号（用于报警）
    public static List<String> getAlarmMobiles() throws SQLException {
        return Config.water.table("water_cfg_whitelist")
                        .whereEq("tag","_alarm")
                        .andEq("type","mobile")
                        .andNeq("value","")
                        .select("value ")
                        .caching(Config.cache_data)
                        .getArray(0);
    }
}
