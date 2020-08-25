package waterapi.controller.cmds;

import waterapi.dso.db.DbWaterRegApi;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_sev_unreg extends CMDBase {
    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String service = get("service");
        String address = get("address");

        if (checkParamsIsOk(service, address) == false) {
            return;
        }

        String note = get("note","");


        DbWaterRegApi.delService(service, address, note);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
