package waterapp.controller.cmds;

import waterapp.dso.db.DbWaterRegApi;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_sev_set extends CMDBase {
    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String service = get("service");
        String address = get("address");

        String note = get("note","");
        int enabled = getInt("enabled",9);

        if(enabled > 1){
            return;
        }

        if (checkParamsIsOk(service, address) == false) {
            return;
        }

        DbWaterRegApi.disableService(service, address, note, enabled>0);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
