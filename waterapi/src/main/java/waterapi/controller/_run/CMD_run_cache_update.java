package waterapi.controller._run;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.protocol.ProtocolHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CMD_run_cache_update extends UapiBase {
    static Logger log = LoggerFactory.getLogger(CMD_run_cache_update.class);

    @Mapping("/_run/cache/update/")
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

    private void handlerDo(String tag) throws Exception {
        if (tag.indexOf(":") < 0) {
            return;
        }

        String[] ss = tag.split(":");

        if ("logger".equals(ss[0])) {
            if (ProtocolHub.logSourceFactory != null) {
                ProtocolHub.logSourceFactory.updateSource(ss[1]); //尝试更新源
                log.info("Logger source updated: {}", ss[1]);
            }
            return;
        }

        //todo: 要不要加？
        if ("broker".equals(ss[0])) {
            if (ProtocolHub.msgBrokerFactory != null) {
                ProtocolHub.msgBrokerFactory.updateBroker(ss[1]); //尝试更新源
            }
            return;
        }
    }
}