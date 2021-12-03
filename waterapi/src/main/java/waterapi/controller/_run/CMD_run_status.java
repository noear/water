package waterapi.controller._run;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.utils.RuntimeStatus;
import org.noear.water.utils.RuntimeUtils;
import waterapi.Config;
import waterapi.controller.UapiBase;
import waterapi.dso.interceptor.Logging;

/**
 * 运行时状态
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Logging
@Whitelist
@Controller
public class CMD_run_status extends UapiBase {
    @Mapping("/_run/status/")
    public Result cmd_exec() throws Exception {
        RuntimeStatus rs = RuntimeUtils.getStatus();
        rs.name = Config.water_service_name;
        rs.address = Config.localHost;

        return Result.succeed(rs);
    }
}
