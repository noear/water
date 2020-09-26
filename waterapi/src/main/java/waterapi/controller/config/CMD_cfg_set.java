package waterapi.controller.config;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.WW;
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
@XController
public class CMD_cfg_set extends UapiBase {
    @NotEmpty({"tag", "key"})
    @XMapping("/cfg/set/")
    public XResult cmd_exec(XContext ctx, String tag, String key, String value) throws Throwable {

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
            DbWaterMsgApi.addMessage(IDUtils.buildGuid(), trace_id, Config.water_service_name, WW.msg_uconfig_topic, tag + "::" + key, null);
        }

        return XResult.succeed();
    }
}
