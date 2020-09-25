package waterapi.controller.run;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 2017/7/19.
 */
@Whitelist
@XController
public class CMD_run_push extends UapiBase {


//    protected boolean isLogging() {
//        return false;
//    }

    @NotEmpty({"msg", "target"})
    @XMapping("/run/push/")
    public XResult cmd_exec(String msg, String target) throws Exception {

        List<String> list = new ArrayList<String>();
        for (String str : target.split(",")) {
            if (str.equals("@alarm")) {
                List<String> mobiles = DbWaterCfgApi.getAlarmMobiles();

                list.addAll(mobiles);
            } else {
                list.add(str);
            }
        }

        String rest = ProtocolHub.heihei.push(Config.water_service_name, list, msg);

        if (TextUtils.isEmpty(rest) == false) {
            return XResult.succeed(ONode.load(rest));
        } else {
            return XResult.failure();
        }
    }
}
