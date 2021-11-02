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
import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class WaterMsgStoreController {

    @Post
    @Mapping("/ajax/init/water_msg")
    public Result ajax_connect(String config) throws Exception {
        if (Config.water == null) {
            return Result.failure("未连接数据库，刷新再试...");
        }

        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }

        DbWaterCfgApi.updConfig(WW.water, Config.water_setup_step, "5");

        //2.
        return Result.succeed(null, "配置成功");
    }
}
