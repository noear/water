package watersetup.controller;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.Props;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import watersetup.Config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author noear 2021/10/31 created
 */
@Controller
public class HomeController extends BaseController {
    final String rdb_water_tml = "schema=water\nserver=localhost:3306\nusername=\npassword=";
    final String rdb_tml = "schema=\nserver=\nusername=\npassword=";

    @Mapping("/")
    public ModelAndView home() {
        if (Config.water == null) {
            viewModel.put("config", rdb_water_tml);

            return view("setup_init");
        } else {
            return view("setup");
        }
    }

    final String water_cfg_properties = "water_cfg_properties";

    @Mapping("/ajax/connect")
    public Result ajax_connect(String config) throws Exception {
        if (Config.water == null) {
            Properties prop = Utils.buildProperties(config);

            if (config.contains("water.ds.")) {
                prop = new Props(prop).getProp("water.ds");
            }

            if (prop.size() == 4) {
                DbContext db = Config.getDb(prop);

                if (db == null) {
                    return Result.failure("链接失败");
                } else {
                    try {
                        tryInitSchema(db);
                    } catch (SQLException e) {
                        EventBus.push(e);
                        return Result.failure("初始化失败..");
                    }
                }
            }
        }

        return Result.succeed();
    }


    private void tryInitSchema(DbContext db) throws Exception {
        Map map = db.sql("SHOW TABLES LIKE ?", water_cfg_properties).getMap();


        if (map.size() > 0) {
            if (db.table(water_cfg_properties).selectCount() > 0) {
                //说明已存在表，且有数据
                Config.water = db;
                return;
            }
        }


        db.setAllowMultiQueries(true);

        tryInitWaterPaas(db);

        //Config.water = db;
    }

    private void tryInitWater(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitSchemaByJsonSql(db, "water_cfg_broker", "water");
        tryInitSchemaByJsonSql(db, "water_cfg_logger", "water");
        tryInitSchemaByJsonSql(db, "water_cfg_properties", "water");
        tryInitSchemaByJsonSql(db, "water_cfg_whitelist", "water");

        tryInitSchemaByJsonSql(db, "water_tool_monitor", "water");
        tryInitSchemaByJsonSql(db, "water_tool_report", "water");
        tryInitSchemaByJsonSql(db, "water_tool_synchronous", "water");
    }

    private void tryInitWaterBcf(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_bcf.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitSchemaByJsonSql(db, "bcf_config", "water_bcf");
        tryInitSchemaByJsonSql(db, "bcf_group", "water_bcf");
        tryInitSchemaByJsonSql(db, "bcf_resource", "water_bcf");
        tryInitSchemaByJsonSql(db, "bcf_resource_linked", "water_bcf");

        tryInitSchemaByJsonSql(db, "bcf_user", "water_bcf");
        tryInitSchemaByJsonSql(db, "bcf_user_linked", "water_bcf");
    }

    private void tryInitWaterPaas(DbContext db) throws Exception {
        String sql = Utils.getResourceAsString("db/water_paas.sql");
        tryInitSchemaBySplitSql(db, sql);

        tryInitSchemaByJsonSql(db, "paas_file", "water_paas");

        tryInitSchemaByJsonSql(db, "rubber_actor", "water_paas");
        tryInitSchemaByJsonSql(db, "rubber_block", "water_paas");

        tryInitSchemaByJsonSql(db, "rubber_model", "water_paas");
        tryInitSchemaByJsonSql(db, "rubber_model_field", "water_paas");
        tryInitSchemaByJsonSql(db, "rubber_scheme", "water_paas");
        tryInitSchemaByJsonSql(db, "rubber_scheme_node", "water_paas");
        tryInitSchemaByJsonSql(db, "rubber_scheme_node_design", "water_paas");
        tryInitSchemaByJsonSql(db, "rubber_scheme_rule", "water_paas");
    }


    private void tryInitSchemaBySplitSql(DbContext db, String sql) throws Exception {
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

    private void tryInitSchemaByJsonSql(DbContext db, String table, String dir) throws Exception {
        String fileName = "db/init/" + dir + "_" + table + ".json";
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
