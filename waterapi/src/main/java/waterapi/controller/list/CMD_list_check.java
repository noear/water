package waterapi.controller.list;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.WW;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgSafeApi;
import waterapi.dso.interceptor.Logging;


/**
 * 名单检测
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_list_check extends UapiBase {
    @NotEmpty({"type", "value"})
    @Mapping("/list/check/")
    public String cmd_exec(Context ctx, String type, String value) throws Exception {
        String tags = ctx.param("tags", "");

        if (tags.contains("client")) {
            if (DbWaterCfgSafeApi.whitelistIgnoreClient()) {
                return "OK";
            }
        }

        if (WW.whitelist_tag_server.equals(tags)) {
            //令牌验证
            if (WW.whitelist_type_token.equals(type)) {
                if (DbWaterCfgSafeApi.isWhitelistByToken(value)) {
                    return "OK";
                } else {
                    return (value + ",not in token list!");
                }
            }

            //ip验证
            if (WW.whitelist_type_ip.equals(type)) {
                if (DbWaterCfgSafeApi.isWhitelistByIp(value)) {
                    return "OK";
                } else {
                    return (value + ",not in ip list!");
                }
            }
        }

        if (DbWaterCfgSafeApi.isWhitelist(tags, type, value)) {
            return ("OK");
        } else {
            return (value + ",not in ip list!");
        }
    }
}
