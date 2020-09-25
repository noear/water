package waterapi.controller.register;

import org.noear.solon.XUtil;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.db.DbWaterRegApi;
import waterapi.dso.interceptor.Logging;

/**
 * 服务注册
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@XController
public class CMD_sev_reg extends UapiBase {
    @NotEmpty({"service", "address"})
    @XMapping("/sev/reg/")
    public XResult cmd_exec(XContext ctx, String service, String address, String meta, int is_unstable, String check_url, int check_type) throws Exception {
        if (meta == null) {
            meta = ctx.param("note");
        }

        String alarm_mobile = ctx.param("alarm_mobile", "");

        if (check_type == 0) {
            if (XUtil.isEmpty(check_url)) {
                throw UapiCodes.CODE_13("check_url");
            }
        }

        if (meta == null) {
            meta = "";
        }


        DbWaterRegApi.addService(service, address, meta, alarm_mobile, check_url, check_type, is_unstable > 0);

        return XResult.succeed();
    }
}
