package wateradmin.dso.db;

import org.noear.water.dso.GatewayUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.water_cfg.GatewayModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 */
public class DbWaterCfgGatewayApi {
    private static DbContext db() {
        return Config.water;
    }


    public static int saveGateway(int gatewayId, String tag, String name, String agent, String policy, int is_enabled) throws SQLException {
        DbTableQuery tb = db().table("water_cfg_gateway")
                .set("tag", tag.trim())
                .set("name", name.trim())
                .set("agent", agent.trim())
                .set("policy", policy.trim())
                .set("is_enabled", is_enabled);

        if (gatewayId > 0) {
            tb.whereEq("gateway_id", gatewayId).update();
            GatewayUtils.notice(tag, name);
            return gatewayId;
        } else {
            return (int) tb.insert();
        }
    }


    public static List<GatewayModel> getGatewayList() throws SQLException {
        return db().table("water_cfg_gateway")
                .orderByAsc("name")
                .selectList("*", GatewayModel.class);
    }

    public static GatewayModel getGateway(int gatewayId) throws SQLException {
        return db().table("water_cfg_gateway")
                .whereEq("gateway_id", gatewayId)
                .selectItem("*", GatewayModel.class);
    }
}
