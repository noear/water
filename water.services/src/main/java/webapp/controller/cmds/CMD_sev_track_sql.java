package webapp.controller.cmds;

import waterapi.dao.db.DbLogApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_sev_track_sql extends CMDBase {

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String service = get("service");

        if (checkParamsIsOk(service) == false) {
            return;
        }

        String schema = get("schema", "");
        long interval = getlong("interval");
        String cmd_sql = get("cmd_sql", "");
        String cmd_arg = get("cmd_arg");

        String operator = get("operator");
        String operator_ip = get("operator_ip");
        String path = get("path");
        String ua = get("ua");
        String note = get("note");


        DbLogApi.addTrack(service,schema,interval,cmd_sql,cmd_arg,operator,operator_ip,path,ua,note);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
