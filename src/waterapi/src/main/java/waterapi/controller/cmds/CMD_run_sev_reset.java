package waterapi.controller.cmds;

import waterapi.dao.db.DbMsgApi;
import waterapi.dao.db.DbSevApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_run_sev_reset extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {

        DbSevApi.setState1As0();


        data.set("code", 1);
        data.set("msg", "success");
    }
}
