package webapp.controller.cmds;


import webapp.dso.db.DbServiceApi;

public class CMD_sev_reg extends CMDBase {
    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String service = get("service");
        String address = get("address");
        String note = get("note");
        String alarm_mobile = get("alarm_mobile");

        if(alarm_mobile == null){
            alarm_mobile = "";
        }

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

        if (note == null) {
            note = "";
        }


        DbServiceApi.addService(service, address, note, alarm_mobile,check_url, check_type);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
