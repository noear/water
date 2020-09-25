package waterapi.controller.run;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.utils.RuntimeStatus;
import org.noear.water.utils.RuntimeUtils;
import waterapi.Config;
import waterapi.controller.UapiBase;

/**
 * 运行时状态
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@XController
public class CMD_run_status extends UapiBase {
    @XMapping("/run/status/")
    public XResult cmd_exec() throws Exception {
        RuntimeStatus rs = RuntimeUtils.getStatus();
        rs.name = Config.water_service_name;
        rs.address = Config.localHost;

        return XResult.succeed(rs);
    }
}
