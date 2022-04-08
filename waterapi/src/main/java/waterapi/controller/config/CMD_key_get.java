package waterapi.controller.config;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
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
 * @since 2022.04
 */
@Logging
@Whitelist
@Controller
public class CMD_key_get extends UapiBase {

    @Mapping("/key/get/")
    public Result cmd_exec(String accessKey, int keyId) throws Throwable {
        if (keyId > 0) {
            KeyM keyM = DbWaterCfgApi.getKeyById(keyId);
            return Result.succeed(keyM);
        }

        if (Utils.isNotEmpty(accessKey)) {
            KeyM keyM = DbWaterCfgApi.getKey(accessKey);
            return Result.succeed(keyM);
        }

        return Result.failure("Invalid parameter");
    }
}
