package waterpaas.dso;

import org.noear.weed.DbContext;
import waterpaas.Config;
import waterpaas.models.LoggerModel;

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
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
