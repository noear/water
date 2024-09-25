package waterapi.controller.register;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.interceptor.Logging;

/**
 * 服务启用状态设置
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_sev_set extends UapiBase {

    @NotEmpty({"service", "address"})
    @Mapping("/sev/set/")
    public Result cmd_exec(Context ctx, String tag, String service, String address, String meta) throws Exception {
        if (meta == null) {
            meta = ctx.paramOrDefault("note", "");
        }

        int enabled = ctx.paramAsInt("enabled", 9);

        DbWaterRegApi.disableService(tag, service, address, meta, enabled > 0);

        return Result.succeed();
    }
}