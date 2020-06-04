package waterapp.controller.cmds;

import org.noear.water.utils.TextUtils;
import waterapp.dso.db.DbWaterMsgApi;

/**
 * Created by noear on 2017/7/19.
 */
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
            DbWaterMsgApi.cancelMessage(key);
        } else {
            DbWaterMsgApi.cancelMsgDistribution(key, subscriber_key);
        }
    }
}
