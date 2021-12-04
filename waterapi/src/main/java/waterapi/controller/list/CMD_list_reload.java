package waterapi.controller.list;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgSafeApi;
import waterapi.dso.interceptor.Logging;

/**
 * 名单重新加载
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_list_reload extends UapiBase {
    @Mapping("/list/reload/")
    public Result cmd_exec() {
        try {
            DbWaterCfgSafeApi.loadWhitelist();

            return Result.succeed();
        } catch (Exception ex) {
            return Result.failure(ex.getMessage());
        }
    }
}
