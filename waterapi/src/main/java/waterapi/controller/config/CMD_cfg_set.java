package waterapi.controller.config;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import waterapi.Config;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.IDUtils;
import waterapi.dso.LockUtils;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.db.DbWaterMsgApi;
import waterapi.dso.interceptor.Logging;
import waterapi.models.ConfigModel;

/**
 * 设置配置
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_cfg_set extends UapiBase {
    @NotEmpty({"tag", "key"})
    @Mapping("/cfg/set/")
    public Result cmd_exec(Context ctx, String tag, String key, String value) throws Throwable {

        if (LockUtils.tryLock(Config.water_service_name, tag + "/" + key) == false) {
            throw UapiCodes.CODE_15;
        }

        //
        // 此处非线程安全
        //
        boolean isOk = false;
        ConfigModel cfg = DbWaterCfgApi.getConfig(tag, key);
        if (key.equals(cfg.key)) {
            if (cfg.is_editable) {
                DbWaterCfgApi.setConfig(tag, key, value);
                isOk = true;
            }
        } else {
            DbWaterCfgApi.addConfig(tag, key, value);
            isOk = true;
        }

        //发消息通知
        if (isOk) {
            String trace_id = ctx.header(WW.http_header_trace);
            ProtocolHub.messageSource().addMessage(IDUtils.buildGuid(), trace_id, Config.water_service_name, WW.msg_uconfig_topic, tag + "::" + key, null);
        }

        return Result.succeed();
    }
}
