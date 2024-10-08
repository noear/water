package waterapi.controller.run;

import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.Config;
import waterapi.dso.interceptor.Logging;

import java.util.ArrayList;
import java.util.List;


/**
 * 嘿嘿推送（todo: 将弃用，移到 /alarm 下，需要各方检查并修改）
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Deprecated
@Logging
@Whitelist
@Controller
public class CMD_run_push extends UapiBase {

    @NotEmpty({"msg", "target"})
    @Mapping("/run/push/")
    public Result cmd_exec(String msg, String target) throws Exception {

        List<String> list = new ArrayList<String>();
        for (String str : target.split(",")) {
            if (str.startsWith("@")) {
                if (str.length() > 2) {
                    List<String> mobiles = DbWaterCfgApi.getAlarmMobiles(str.replace("@", "_"));

                    list.addAll(mobiles);
                }
            } else {
                list.add(str);
            }
        }

        String rest = ProtocolHub.heihei.push(Config.water_service_name, list, msg);

        if (TextUtils.isNotEmpty(rest)) {
            return Result.succeed(ONode.load(rest));
        } else {
            return Result.failure();
        }
    }
}
