package watersetup.controller.init;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.weed.DbContext;
import watersetup.Config;
import watersetup.dso.InitUtils;
import watersetup.dso.db.DbWaterCfgApi;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class WaterController {

    @Post
    @Mapping("/ajax/connect/water")
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
        if(InitUtils.allowWaterInit(db) == false){
            Config.water = db;
            return;
        }


        db.setAllowMultiQueries(true);

        InitUtils.tryInitWater(db);

        Config.water = db;

        String schema = props.getProperty("schema");
        String password = props.getProperty("password");
        String username = props.getProperty("username");
        String url = props.getProperty("url");


        //更新配置
        StringBuilder cfgBuf = new StringBuilder();
        cfgBuf.append("schema=").append(schema).append("\n");
        cfgBuf.append("url=").append(url).append("\n");
        cfgBuf.append("password=").append(password).append("\n");
        cfgBuf.append("username=").append(username).append("\n");
        cfgBuf.append("jdbcUrl=${url}\n");

        DbWaterCfgApi.updConfig(WW.water, WW.water, cfgBuf.toString());
        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "1");
    }
}
