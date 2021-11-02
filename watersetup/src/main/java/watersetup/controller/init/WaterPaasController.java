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
public class WaterPaasController {
    final String water_check_table = "paas_file";

    @Post
    @Mapping("/ajax/connect/water_bcf")
    public Result ajax_connect(String config) throws Exception {
        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }

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

        return Result.succeed();
    }


    private void tryInitSchema(Properties props, DbContext db) throws Exception {
        if(InitUtils.allowWaterPaasInit(db) == false){
            return;
        }


        db.setAllowMultiQueries(true);

        InitUtils.tryInitWaterPaas(db);

        DbWaterCfgApi.updConfig(WW.water, "water_setup", "3");
    }
}
