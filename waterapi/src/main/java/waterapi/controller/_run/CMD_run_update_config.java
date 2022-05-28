package waterapi.controller._run;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import waterapi.Config;
import waterapi.controller.UapiBase;
import waterapi.dso.interceptor.Logging;

/**
 * 缓存更新
 *
 * @author noear
 * @since 2021.10
 */
@Logging
@Whitelist
@Controller
public class CMD_run_update_config extends UapiBase {
    static Logger log = LoggerFactory.getLogger(CMD_run_update_config.class);

    @Mapping("/_run/update/config/")
    public Result cmd_exec(String tags) throws Exception {
        if (Utils.isNotEmpty(tags)) {
            for (String tag : tags.split(";")) {
                if (Utils.isEmpty(tag) == false) {
                    handlerDo(tag);
                }
            }
        }

        return Result.succeed();
    }

    private void handlerDo(String tagKey) throws Exception {
        if (ProtocolHub.heihei == null) {
            return;
        }

        if (tagKey.indexOf("::") < 0) {
            return;
        }

        String[] tk = tagKey.split("::");

        if (WW.water.equals(tk[0])) {
            if (WW.water_heihei.equals(tk[1])) {
                ConfigM cfg = Config.cfg(WW.water_heihei);
                ProtocolHub.heihei.updateConfig(cfg);
            }
        }
    }
}