package waterapi.controller.run;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.interceptor.Logging;


/**
 * 白名单检测
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_run_whitelist_check extends UapiBase {
    @NotEmpty({"type", "value"})
    @Mapping("/run/whitelist/check/")
    public String cmd_exec(Context ctx, String type, String value) throws Exception {
        String tags = ctx.param("tags", "");

        if (tags.contains("client")) {
            if (DbWaterCfgApi.whitelistIgnoreClient()) {
                return "OK";
            }
        }

        if (DbWaterCfgApi.isWhitelist(tags, type, value)) {
            return ("OK");
        } else {
            return (value + ",not is whitelist!");
        }
    }
}
