package webapp.controller.cmds;


import org.noear.water.tools.TextUtils;
import webapp.dso.db.DbMsgApi;

public class CMD_msg_cancel extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //消息key //派发时会回传
        String subscriber_key = get("subscriber_key"); //消息key //派发时会回传

        if (checkParamsIsOk(key) == false) {
            return;
        }

        //如果不需要修改，检查是否已存在
        //
        if (TextUtils.isEmpty(subscriber_key)) {
            DbMsgApi.cancelMessage(key);
        } else {
            DbMsgApi.cancelMsgDistribution(key, subscriber_key);
        }
    }
}
