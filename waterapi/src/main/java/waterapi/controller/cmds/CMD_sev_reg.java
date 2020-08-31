package waterapi.controller.cmds;

import waterapi.dso.db.DbWaterRegApi;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_sev_reg extends CMDBase {
    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String service = get("service");
        String address = get("address");
        String meta = get("meta");
        if(meta == null){
            meta = get("note");
        }

        String alarm_mobile = get("alarm_mobile","");
        int is_unstable = getInt("is_unstable", 0);


        String check_url = get("check_url");
        int check_type = getInt("check_type");

        if (checkParamsIsOk(service, address) == false) {
            return;
        }

        if (check_type == 0) {
            if (checkParamsIsOk(check_url) == false) {
                return;
            }
        }

        if (meta == null) {
            meta = "";
        }


        DbWaterRegApi.addService(service, address, meta, alarm_mobile, check_url, check_type, is_unstable > 0);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
