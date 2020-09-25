package waterapi.controller.run;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XResult;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;

/**
 * 运行检测
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@XController
public class CMD_run_check extends UapiBase {
    @XMapping("/run/check/")
    public XResult cmd_exec() throws Exception{
        DbWaterCfgApi.loadWhitelist(); //检测服务时，就会进行白名单刷新

        return XResult.succeed();
    }
}
