package waterapi.controller.log;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.model.LogLevel;
import org.noear.water.model.LogM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.utils.SnowflakeUtils;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.LogPipelineLocal;

import java.util.Date;

/**
 * 添加日志
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@Controller
public class CMD_log_logger extends UapiBase {

    /**
     * @param logger 日志记录器name
     */
    @NotEmpty("logger")
    @Mapping("/log/logger/")
    public Result cmd_exec(String logger) throws Exception {
        if (Utils.isNotEmpty(logger)) {
            if (ProtocolHub.logSourceFactory != null) {
                ProtocolHub.logSourceFactory.updateSource(logger);
            }
        }

        return Result.succeed();
    }
}
