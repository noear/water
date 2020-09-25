package waterapi.controller.run;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;

@Whitelist
@XController
public class CMD_run_whitelist_check extends UapiBase {
    @XMapping("whitelist/check/")
    public String cmd_exec(XContext ctx) throws Exception {
        String tags = ctx.param("tags", "");
        String type = ctx.param("type", "");
        String value = ctx.param("value", "");

        if (tags.contains("client")) {
            if (DbWaterCfgApi.whitelistIgnoreClient()) {
                return "OK";
            }
        }

        boolean isOk = DbWaterCfgApi.isWhitelist(tags, type, value);

        if (isOk) {
            return ("OK");
        } else {
            return (value + ",not is whitelist!");
        }
    }
}
