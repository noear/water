package waterapi.controller.cmds;

import waterapi.dao.db.DbMsgApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_run_msg_clean extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {

        int state = getInt("state");

        DbMsgApi.clean(state);


        data.set("code", 1);
        data.set("msg", "success");
    }
}
