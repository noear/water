package waterapi.controller.register;

import org.noear.solon.XUtil;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
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
@XController
public class CMD_sev_set extends UapiBase {
    /**
     * @param s 指令
     * */
    @XMapping("/sev/set/")
    public XResult cmd_exec(XContext ctx, String s) throws Exception {
        if (TextUtils.isEmpty(s)) {
            String service = ctx.param("service");
            String address = ctx.param("address");

            int enabled = ctx.paramAsInt("enabled", 9);

            return exec0(ctx, service, address, enabled);
        } else {
            // 为运维带来便利
            // s=${service}@${ip:port},${enabled}
            // curl http://water2/sev/set/?s=rockrpc@10.0.0.79:1013,1
            //
            String[] ss = s.split("@");
            String[] ss2 = ss[1].split(",");

            int enabled = Integer.parseInt(ss2[1]);

            return exec0(ctx, ss[0], ss2[0], enabled);
        }
    }

    private XResult exec0(XContext ctx, String service, String address, int enabled) throws Exception {

        if (XUtil.isEmpty(service)) {
            throw UapiCodes.CODE_13("s or service");
        }

        if (XUtil.isEmpty(address)) {
            throw UapiCodes.CODE_13("s or address");
        }

        String note = ctx.param("note", "");

        DbWaterRegApi.disableService(service, address, note, enabled > 0);

        return XResult.succeed();
    }
}
