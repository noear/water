package waterapi.controller.cmds;

import waterapi.dao.HeiheiApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_run_push extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String msg = get("msg");
        String target = get("target");

        if (checkParamsIsOk(target, msg) == false) {
            return;
        }

        if (target.indexOf(',') > 0) {
            List<String> list = new ArrayList<String>();
            for (String str : target.split(",")) {
                list.add(str);
            }

            HeiheiApi.push(list, msg);
        } else {
            HeiheiApi.push(target, msg);
        }

        data.set("code", 1);
        data.set("msg", "success");
    }
}
