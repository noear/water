package waterapi.controller.register;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
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
    /**
     * @param s 指令
     */
    @Mapping("/sev/get/")
    public Result cmd_exec(Context ctx, String s, String meta) throws Exception {
        if (meta == null) {
            meta = ctx.param("note", "");
        }

        if (TextUtils.isEmpty(s)) {
            String service = ctx.param("service");
            String address = ctx.param("address");

            return exec0(ctx, service, address, meta);
        } else {
            // 为运维带来便利
            // s=${service}@${ip:port}
            // curl http://water2/sev/get/?s=rockrpc@10.0.0.79:1013
            //
            String[] ss = s.split("@");

            return exec0(ctx, ss[0], ss[1], meta);
        }
    }

    private Result exec0(Context ctx, String service, String address, String meta) throws Exception {

        if (Utils.isEmpty(service)) {
            throw UapiCodes.CODE_13("s or service");
        }

        if (Utils.isEmpty(address)) {
            throw UapiCodes.CODE_13("s or address");
        }


        ServiceModel serviceModel = DbWaterRegApi.getService(service, address, meta);

        return Result.succeed(serviceModel);
    }
}
