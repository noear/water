package waterapp.controller.cmds;

import org.noear.snack.ONode;
import waterapp.dso.HeiheiApi;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.utils.TextUtils;

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
            if(str.equals("@alarm")){
                List<String> mobiles = DbWaterCfgApi.getAlarmMobiles();

                list.addAll(mobiles);
            }else {
                list.add(str);
            }
        }

        String rest = HeiheiApi.push(list, msg);

        data.set("code", 1);
        data.set("msg", "success");
        if(TextUtils.isEmpty(rest) == false){
            data.set("data", ONode.load(rest));
        }else{
            data.set("data", "null");
        }

    }
}
