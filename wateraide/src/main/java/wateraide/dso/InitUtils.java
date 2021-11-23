package wateraide.dso;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import wateraide.models.water.*;
import wateraide.models.water_bcf.BcfConfigModel;
import wateraide.models.water_bcf.BcfGroupModel;
import wateraide.models.water_bcf.BcfResourceModel;
import wateraide.models.water_bcf.BcfUserModel;
import wateraide.models.water_paas.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/11/2 created
 */
public class InitUtils {

    static final String water_check_table = "water_cfg_properties";
    static final String water_bcf_check_table = "bcf_group";
    static final String water_paas_check_table = "luffy_file";

    public static boolean allowWaterInit(DbContext db) throws SQLException {
        return hasTable(db, water_check_table) == false;
    }

    public static boolean allowWaterBcfInit(DbContext db) throws SQLException {
        return hasTable(db, water_bcf_check_table) == false;
    }

    public static boolean allowWaterPaasInit(DbContext db) throws SQLException {
        return hasTable(db, water_paas_check_table) == false;
    }

    /**
     * 检查一张表是否存在
     */
    private static boolean hasTable(DbContext db, String table) throws SQLException {
        Map map = db.sql("SHOW TABLES LIKE ?", table).getMap();

        if (map.size() > 0) {
            //说明有表
            if (db.table(table).selectCount() > 0) {
                //说明也有数据
                return true;
            }
        }

        return false;
    }

    public static void tryInitWater(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByTypeJsonSql(db, WaterCfgBrokerModel.class, "water_cfg_broker", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgGatewayModel.class, "water_cfg_gateway", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgLoggerModel.class, "water_cfg_logger", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgPropertiesModel.class, "water_cfg_properties", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgWhitelistModel.class, "water_cfg_whitelist", "water");

        tryInitDataByTypeJsonSql(db, WaterToolMonitorModel.class, "water_tool_monitor", "water");
        tryInitDataByTypeJsonSql(db, WaterToolReportModel.class, "water_tool_report", "water");
        tryInitDataByTypeJsonSql(db, WaterToolSynchronousModel.class, "water_tool_synchronous", "water");
    }

    public static void tryInitWaterBcf(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_bcf.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByTypeJsonSql(db, BcfConfigModel.class,"bcf_config", "water_bcf");
        tryInitDataByTypeJsonSql(db, BcfGroupModel.class,"bcf_group", "water_bcf");
        tryInitDataByTypeJsonSql(db, BcfResourceModel.class,"bcf_resource", "water_bcf");
        tryInitDataByJsonSql(db, "bcf_resource_linked", "water_bcf");

        tryInitDataByTypeJsonSql(db, BcfUserModel.class,"bcf_user", "water_bcf");
        tryInitDataByJsonSql(db, "bcf_user_linked", "water_bcf");
    }

    public static void tryInitWaterPaas(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_paas.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByTypeJsonSql(db, LuffyFileModel.class, "luffy_file", "water_paas");

        tryInitDataByTypeJsonSql(db, RubberBlockModel.class, "rubber_block", "water_paas");

        tryInitDataByTypeJsonSql(db, RubberModelModel.class, "rubber_model", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberModelFieldModel.class, "rubber_model_field", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeModel.class, "rubber_scheme", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeNodeModel.class, "rubber_scheme_node", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeNodeDesignModel.class, "rubber_scheme_node_design", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeRuleModel.class, "rubber_scheme_rule", "water_paas");
    }


    private static void tryInitSchemaBySplitSql(DbContext db, String sql) throws Exception {
        if (Utils.isNotEmpty(sql)) {
            for (String sqlItem : sql.split(";")) {
                sqlItem = sqlItem.trim();

                if (Utils.isNotEmpty(sqlItem)) {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>: " + sqlItem);
                    db.exe(sqlItem);
                }
            }
        }
    }

    private static void tryInitDataByJsonSql(DbContext db, String table, String schema) throws Exception {
        String fileName = "db/init/" + schema + "_" + table + ".json";
        System.out.println(">>>>>>>>>>>>>>>>>>>>: " + fileName);

        String json = Utils.getResourceAsString(fileName);


        ONode array = ONode.loadStr(json);
        List<DataItem> dataItems = new ArrayList<>();
        for (ONode n1 : array.ary()) {
            dataItems.add(new DataItem().setMap(n1.toObject(Map.class)));
        }

        if (dataItems.size() > 0) {
            db.table(table).insertList(dataItems);
        }
    }

    private static <T> void tryInitDataByTypeJsonSql(DbContext db, Class<T> clz, String table, String schema) throws Exception {
        String fileName = "db/init/" + schema + "_" + table + ".json";
        System.out.println(">>>>>>>>>>>>>>>>>>>>: " + fileName);

        String json = Utils.getResourceAsString(fileName);
        if(Utils.isEmpty(json)){
            return;
        }

        //增加 Base64 解码支持
        if(json.startsWith("[") == false){
            json = Base64Utils2.decode(json);
        }

        ONode array = ONode.loadStr(json);
        List<T> dataItems = new ArrayList<>();
        for (ONode n1 : array.ary()) {
            dataItems.add(n1.toObject(clz));
        }

        if (dataItems.size() > 0) {
            db.mapperBase(clz).insertList(dataItems);
        }
    }
}
