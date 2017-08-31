package waterapi.controller.cmds;

import waterapi.dao.HeiheiApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_run_puch extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String name = get("name");
        String txt = get("txt");

        if (checkParamsIsOk(name, txt) == false) {
            return;
        }

        HeiheiApi.push(name,txt);

        data.set("code", 1);
        data.set("msg", "success");
    }
}
