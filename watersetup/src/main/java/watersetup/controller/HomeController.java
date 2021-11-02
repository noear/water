package watersetup.controller;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.weed.DbContext;
import watersetup.Config;

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

        String sql = Utils.getResourceAsString("static/_db/water.sql");
        if (Utils.isNotEmpty(sql)) {
            for (String sqlItem : sql.split(";")) {
                System.out.println(">>>>" + sqlItem);
                db.exe(sqlItem);
            }
        }

        sql = Utils.getResourceAsString("static/_db/water.sql_init.sql");
        if (Utils.isNotEmpty(sql)) {
            for (String sqlItem : sql.split(";")) {
                System.out.println(">>>>" + sqlItem);
                db.exe(sqlItem);
            }
        }

        Config.water = db;
    }
}
