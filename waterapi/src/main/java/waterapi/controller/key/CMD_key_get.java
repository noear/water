package waterapi.controller.key;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.model.KeyM;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.interceptor.Logging;

/**
 * 获取配置
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_key_get extends UapiBase {

    @NotEmpty("accessKey")
    @Mapping("/key/get/")
    public Result cmd_exec(Context ctx, String accessKey) throws Throwable {
        KeyM keyM = DbWaterCfgApi.getKey(accessKey);
        return Result.succeed(keyM);
    }
}
