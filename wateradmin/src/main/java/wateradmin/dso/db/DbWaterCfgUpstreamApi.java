package wateradmin.dso.db;

import org.noear.solon.Utils;
import org.noear.water.dso.GatewayUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateradmin.Config;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.GatewayModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 */
public class DbWaterCfgUpstreamApi {
    private static DbContext db() {
        return Config.water;
    }


    public static int saveGateway(int gatewayId, String tag, String name, String agent, String policy, int is_enabled) throws SQLException {
        DbTableQuery tb = db().table("water_cfg_gateway")
                .set("tag", tag.trim())
                .set("name", name.trim())
                .set("agent", agent.trim())
                .set("policy", policy.trim())
                .set("is_enabled", is_enabled)
                .set("gmt_modified", System.currentTimeMillis());

        if (gatewayId > 0) {
            tb.whereEq("gateway_id", gatewayId).update();
            GatewayUtils.notice(tag, name);
            return gatewayId;
        } else {
            return (int) tb.insert();
        }
    }

    public static List<TagCountsModel> getGatewayTagList() throws SQLException {
        return db().table("water_cfg_gateway")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag, count(*) counts", TagCountsModel.class);
    }

    public static List<TagCountsModel> getGatewayTagListByEnabled() throws SQLException {
        return db().table("water_cfg_gateway")
                .whereEq("is_enabled", 1)
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag, count(*) counts", TagCountsModel.class);
    }


    public static List<GatewayModel> getGatewayList(String tag_name, int is_enabled) throws SQLException {
        DbTableQuery qr = db().table("water_cfg_gateway")
                .whereEq("is_enabled", is_enabled);

        if ("_".equals(tag_name)) {
            qr.andEq("tag", "");
        } else {
            if (Utils.isNotEmpty(tag_name)) {
                qr.andEq("tag", tag_name);
            }
        }

        return qr.orderByAsc("name")
                .selectList("*", GatewayModel.class);
    }

    public static GatewayModel getGateway(int gatewayId) throws SQLException {
        return db().table("water_cfg_gateway")
                .whereEq("gateway_id", gatewayId)
                .selectItem("*", GatewayModel.class);
    }

    //设置启用状态
    public static void setGatewayEnabled(int gatewayId, int is_enabled) throws SQLException {
        db().table("water_cfg_gateway")
                .whereEq("gateway_id", gatewayId)
                .set("is_enabled", is_enabled)
                .set("gmt_modified", System.currentTimeMillis())
                .update();
    }

    public static void delGateway(int gatewayId) throws SQLException {
        db().table("water_cfg_gateway")
                .whereEq("gateway_id", gatewayId)
                .delete();
    }
}
