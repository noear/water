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
import waterapi.models.ServiceModel;

/**
 * @author noear 2020/12/31 created
 */
@Logging
@Whitelist
@Controller
public class CMD_sev_get extends UapiBase {

    @NotEmpty({"service", "address"})
    @Mapping("/sev/get/")
    public Result cmd_exec(Context ctx, String tag, String service, String address, String meta) throws Exception {
        if (meta == null) {
            meta = ctx.param("note", "");
        }

        ServiceModel serviceModel = DbWaterRegApi.getService(service, address, meta);

        return Result.succeed(serviceModel);
    }
}
