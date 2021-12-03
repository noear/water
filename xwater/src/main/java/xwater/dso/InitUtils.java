package xwater.dso;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.weed.DbContext;
import xwater.models.data.grit.GritResourceDo;
import xwater.models.data.grit.GritResourceLinkedDo;
import xwater.models.data.grit.GritSubjectDo;
import xwater.models.data.grit.GritSubjectLinkedDo;
import xwater.models.data.water.*;
import xwater.models.data.water_paas.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/11/2 created
 */
public class InitUtils {

    static final String water_check_table = "water_cfg_properties";
    static final String water_paas_check_table = "luffy_file";
    static final String grit_check_table = "grit_resource";

    public static boolean allowWaterInit(DbContext db) throws SQLException {
        return hasTable(db, water_check_table) == false;
    }

    public static boolean allowWaterPaasInit(DbContext db) throws SQLException {
        return hasTable(db, water_paas_check_table) == false;
    }

    public static boolean allowGritInit(DbContext db) throws SQLException {
        return hasTable(db, grit_check_table) == false;
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

    public static void tryInitGrit(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/grit.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByTypeJsonSql(db, GritResourceDo.class,"grit_resource", "grit");
        tryInitDataByTypeJsonSql(db, GritResourceLinkedDo.class,"grit_resource_linked", "grit");
        tryInitDataByTypeJsonSql(db, GritSubjectDo.class,"grit_subject", "grit");
        tryInitDataByTypeJsonSql(db, GritSubjectLinkedDo.class,"grit_subject_linked", "grit");
    }

    public static void tryInitWater(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByTypeJsonSql(db, WaterCfgBrokerDo.class, "water_cfg_broker", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgGatewayDo.class, "water_cfg_gateway", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgLoggerDo.class, "water_cfg_logger", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgPropertiesDo.class, "water_cfg_properties", "water");
        tryInitDataByTypeJsonSql(db, WaterCfgWhitelistDo.class, "water_cfg_whitelist", "water");

        tryInitDataByTypeJsonSql(db, WaterToolMonitorDo.class, "water_tool_monitor", "water");
        tryInitDataByTypeJsonSql(db, WaterToolReportDo.class, "water_tool_report", "water");
        tryInitDataByTypeJsonSql(db, WaterToolSynchronousDo.class, "water_tool_synchronous", "water");
    }


    public static void tryInitWaterPaas(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_paas.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByTypeJsonSql(db, LuffyFileDo.class, "luffy_file", "water_paas");

        tryInitDataByTypeJsonSql(db, RubberBlockDo.class, "rubber_block", "water_paas");

        tryInitDataByTypeJsonSql(db, RubberModelDo.class, "rubber_model", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberModelFieldDo.class, "rubber_model_field", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeDo.class, "rubber_scheme", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeNodeDo.class, "rubber_scheme_node", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeNodeDesignDo.class, "rubber_scheme_node_design", "water_paas");
        tryInitDataByTypeJsonSql(db, RubberSchemeRuleDo.class, "rubber_scheme_rule", "water_paas");
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
