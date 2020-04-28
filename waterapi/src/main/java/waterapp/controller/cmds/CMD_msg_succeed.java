package waterapp.controller.cmds;

import waterapp.utils.TextUtils;
import waterapp.dso.db.DbWaterMsgApi;

/**
 * Created by noear on 2017/7/19.
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
            DbWaterMsgApi.succeedMessage(key);
        }else {
            DbWaterMsgApi.succeedMsgDistribution(key, subscriber_key);
        }
    }
}
