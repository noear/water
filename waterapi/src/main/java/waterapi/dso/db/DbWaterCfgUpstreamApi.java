package waterapi.dso.db;

import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.models.GatewayModel;

import java.sql.SQLException;

/**
 * @author noear
 */
public class DbWaterCfgUpstreamApi {
    private static DbContext db() {
        return Config.water;
    }

    public static GatewayModel getGatewayByName(String name) throws SQLException {
        return db().table("water_cfg_gateway")
                .whereEq("name", name)
                .limit(1)
                .selectItem("*", GatewayModel.class);
    }
}
