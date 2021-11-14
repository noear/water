package wateraide.controller.init;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.weed.DbContext;
import wateraide.Config;
import wateraide.dso.InitUtils;
import wateraide.dso.db.DbWaterCfgApi;

import java.sql.SQLException;
import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class Init3PaasController {

    @Post
    @Mapping("/ajax/init/water_paas")
    public Result ajax_connect(String config) {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        if (Utils.isEmpty(config)) {
            return Result.failure("出错，配置不能为空");
        }

        Properties props = Config.getProp(config);

        if (props.size() > 3) {
            try (DbContext db = Config.getDb(props, true)) {
                tryInitSchema(db, config);
            } catch (Exception e) {
                EventBus.push(e);
                return Result.failure("出错，" + e.getLocalizedMessage());
            }
        } else {
            return Result.failure("出错，配置有问题...");
        }

        return Result.succeed(null, "配置成功");
    }


    private void tryInitSchema(DbContext db, String config) throws Exception {
        if (InitUtils.allowWaterPaasInit(db) == false) {
            DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "3");
            return;
        }


        db.setAllowMultiQueries(true);

        InitUtils.tryInitWaterPaas(db);

        //更新配置
        DbWaterCfgApi.updConfig(WW.water, WW.water_paas, config);
        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "3");
    }
}
