package watersetup.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.weed.DbContext;
import watersetup.Config;
import watersetup.dso.InitUtils;

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

    @Post
    @Mapping("/ajax/connect")
    public Result ajax_connect(String config) throws Exception {
        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }

        if (Config.water == null) {
            Properties props = Config.getProp(config);

            if (props.size() > 3) {
                DbContext db = Config.getDb(props);

                if (db == null) {
                    return Result.failure("连接失败");
                } else {
                    try {
                        tryInitSchema(props, db);
                    } catch (SQLException e) {
                        EventBus.push(e);
                        return Result.failure("初始化失败..");
                    }
                }
            } else {
                return Result.failure("配置有问题...");
            }
        }

        if (Config.water == null) {
            return Result.failure("连接失败");
        }

        return Result.succeed();
    }


    private void tryInitSchema(Properties props, DbContext db) throws Exception {
        Map map = db.sql("SHOW TABLES LIKE ?", water_cfg_properties).getMap();

        if (map.size() > 0) {
            //说明有表
            if (db.table(water_cfg_properties).selectCount() > 0) {
                //说明也有数据
                Config.water = db;
                return;
            }
        }


        db.setAllowMultiQueries(true);

        InitUtils.tryInitWater(db);
        InitUtils.tryInitWaterBcf(db);
        InitUtils.tryInitWaterPaas(db);

        Config.water = db;
    }
}
