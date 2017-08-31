package waterapi.controller.cmds;

import waterapi.dao.DisttimeUtil;
import waterapi.dao.db.DbMsgApi;

import java.util.Date;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_msg_cancel extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //消息key //派发时会回传

        if (checkParamsIsOk(key) == false) {
            return;
        }

        //如果不需要修改，检查是否已存在
        //
        DbMsgApi.cancelMessage(key);


        data.set("code", 0);
        data.set("msg", "unknown");
    }
}
