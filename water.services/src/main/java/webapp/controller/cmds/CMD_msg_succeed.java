package webapp.controller.cmds;

import waterapi.dao.db.DbMsgApi;
import waterapi.utils.TextUtils;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_msg_succeed extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //消息key //派发时会回传
        String subscriber_key = get("subscriber_key"); //消息key //派发时会回传

        if (checkParamsIsOk(key) == false) {
            return;
        }

        if(TextUtils.isEmpty(subscriber_key)){
            DbMsgApi.succeedMessage(key);
        }else {
            DbMsgApi.succeedMsgDistribution(key, subscriber_key);
        }
    }
}
