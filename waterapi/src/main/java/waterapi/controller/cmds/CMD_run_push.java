package waterapi.controller.cmds;

import org.noear.snack.ONode;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_run_push extends CMDBase {

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {

        String msg = get("msg");
        String target = get("target");

        if (checkParamsIsOk(target, msg) == false) {
            return;
        }


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

        data.set("code", 1);
        data.set("msg", "success");
        if (TextUtils.isEmpty(rest) == false) {
            data.set("data", ONode.load(rest));
        } else {
            data.set("data", "null");
        }

    }
}
