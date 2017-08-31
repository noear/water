package waterapi.controller.cmds;

import waterapi.dao.db.DbMsgApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_run_msg_reset extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {

        DbMsgApi.setState1As0();


        data.set("code", 1);
        data.set("msg", "success");
    }
}
