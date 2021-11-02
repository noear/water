package watersetup.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.weed.DbContext;
import watersetup.Config;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.SQLException;
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
            //说明已存在表
            Config.water = db;
            return;
        }


        db.setAllowMultiQueries(true);

        String sql;

        sql = Utils.getResourceAsString("static/_db/water.sql");
        tryInitSchemaBySplitSql(db, sql);

//        sql = Utils.getResourceAsString("static/_db/water.sql_init.sql");
//        tryInitSchemaByLineSql(db, sql);

        sql = Utils.getResourceAsString("static/_db/water_bcf.sql");
        tryInitSchemaBySplitSql(db, sql);
//
//        sql = Utils.getResourceAsString("static/_db/water_bcf.sql_init.sql");
//        tryInitSchemaByLineSql(db, sql);

        sql = Utils.getResourceAsString("static/_db/water_paas.sql");
        tryInitSchemaBySplitSql(db, sql);

        //Config.water = db;
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

    private void tryInitSchemaByLineSql(DbContext db, String sql) throws Exception {
        if (Utils.isNotEmpty(sql)) {
            System.out.println(sql);
            db.exe(sql);

//            BufferedReader reader = new BufferedReader(new StringReader(sql));
//            String sqlItem;
//
//            while ((sqlItem = reader.readLine()) != null) {
//                if (Utils.isNotEmpty(sqlItem)) {
//                    System.out.println(">>>>>>>>>>>>>>>>>>>>: " + sqlItem);
//                    db.exe(sqlItem);
//                }
//            }
        }
    }
}
