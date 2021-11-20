package waterapi.controller.run;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.db.DbWaterCfgSafeApi;

/**
 * 运行检测
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Controller
public class CMD_run_check extends UapiBase {
    @Mapping("/run/check/")
    public Result cmd_exec() throws Exception{
        DbWaterCfgSafeApi.loadWhitelist(); //检测服务时，就会进行白名单刷新

        return Result.succeed();
    }
}
