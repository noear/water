package waterapi.controller.register;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.interceptor.Logging;

/**
 * 服务注锁
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_sev_unreg extends UapiBase {

    @NotEmpty({"service", "address"})
    @Mapping("/sev/unreg/")
    protected Result cmd_exec(Context ctx, String service, String address, String meta) throws Exception {

        if (meta == null) {
            meta = ctx.param("note"); //旧的
        }

        if (meta == null) {
            meta = "";
        }

        DbWaterRegApi.delService(service, address, meta);

        return Result.succeed();
    }
}
