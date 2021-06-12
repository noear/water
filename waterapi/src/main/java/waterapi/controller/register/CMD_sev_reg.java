package waterapi.controller.register;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.utils.TextUtils;
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
@Controller
public class CMD_sev_reg extends UapiBase {
    /**
     * @param service    服务名
     * @param address    地址
     * @param meta       元信息
     * @param check_type 检测类型
     * @param check_url  检测地址
     */
    @NotEmpty({"service", "address"})
    @Mapping("/sev/reg/")
    public Result cmd_exec(Context ctx, String tag, String service, String address, String meta, int is_unstable, String check_url, String code_location, int check_type) throws Exception {
        if (meta == null) {
            meta = ctx.param("note");
        }

        if (meta == null) {
            meta = "";
        }

        if (tag == null) {
            tag = "";
        }

        String alarm_mobile = ctx.param("alarm_mobile", "");

        if (check_type == 0) {
            if (Utils.isEmpty(check_url)) {
                throw UapiCodes.CODE_13("check_url");
            }
        }


        DbWaterRegApi.addService(tag, service, address, meta, alarm_mobile, check_url, check_type, code_location, is_unstable > 0);

        if (TextUtils.isNotEmpty(service) && TextUtils.isNotEmpty(address)) {
            //记录消费关系
            if (address.contains(":")) {
                DbWaterRegApi.logConsume(Solon.cfg().appName(), service, address);
            }
        }

        return Result.succeed();
    }
}
