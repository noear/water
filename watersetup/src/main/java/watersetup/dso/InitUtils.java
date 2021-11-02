package watersetup.dso;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/11/2 created
 */
public class InitUtils {

    public static void tryInitWater(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByJsonSql(db, "water_cfg_broker", "water");
        tryInitDataByJsonSql(db, "water_cfg_logger", "water");
        tryInitDataByJsonSql(db, "water_cfg_properties", "water");
        tryInitDataByJsonSql(db, "water_cfg_whitelist", "water");

        tryInitDataByJsonSql(db, "water_tool_monitor", "water");
        tryInitDataByJsonSql(db, "water_tool_report", "water");
        tryInitDataByJsonSql(db, "water_tool_synchronous", "water");
    }

    public static void tryInitWaterBcf(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_bcf.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByJsonSql(db, "bcf_config", "water_bcf");
        tryInitDataByJsonSql(db, "bcf_group", "water_bcf");
        tryInitDataByJsonSql(db, "bcf_resource", "water_bcf");
        tryInitDataByJsonSql(db, "bcf_resource_linked", "water_bcf");

        tryInitDataByJsonSql(db, "bcf_user", "water_bcf");
        tryInitDataByJsonSql(db, "bcf_user_linked", "water_bcf");
    }

    public static void tryInitWaterPaas(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_paas.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitDataByJsonSql(db, "paas_file", "water_paas");

        tryInitDataByJsonSql(db, "rubber_actor", "water_paas");
        tryInitDataByJsonSql(db, "rubber_block", "water_paas");

        tryInitDataByJsonSql(db, "rubber_model", "water_paas");
        tryInitDataByJsonSql(db, "rubber_model_field", "water_paas");
        tryInitDataByJsonSql(db, "rubber_scheme", "water_paas");
        tryInitDataByJsonSql(db, "rubber_scheme_node", "water_paas");
        tryInitDataByJsonSql(db, "rubber_scheme_node_design", "water_paas");
        tryInitDataByJsonSql(db, "rubber_scheme_rule", "water_paas");
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

        ONode oNode = ONode.loadStr(json);
        List<DataItem> dataItems = new ArrayList<>();
        for (ONode n1 : oNode.ary()) {
            Map<String, Object> map = n1.toObject(Map.class);
            dataItems.add(new DataItem().setMap(map));
        }

        if (dataItems.size() > 0) {
            db.table(table).insertList(dataItems);
        }
    }
}
