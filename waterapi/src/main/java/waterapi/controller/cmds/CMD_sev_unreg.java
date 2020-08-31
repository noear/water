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

        String meta = get("meta");
        if(meta == null){
            meta = get("note"); //旧的
        }

        if (meta == null) {
            meta = "";
        }


        DbWaterRegApi.delService(service, address, meta);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
