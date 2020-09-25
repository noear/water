package waterapi.controller.sev;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterRegApi;

/**
 * Created by noear on 2017/7/19.
 */
@Whitelist
@XController
public class CMD_sev_unreg extends UapiBase {

    @NotEmpty({"service", "address"})
    @XMapping("/sev/unreg/")
    protected XResult cmd_exec(XContext ctx, String service, String address, String meta) throws Exception {

        if (meta == null) {
            meta = ctx.param("note"); //旧的
        }

        if (meta == null) {
            meta = "";
        }

        DbWaterRegApi.delService(service, address, meta);

        return XResult.succeed();
    }
}
