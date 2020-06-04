package waterapp.controller.cmds;

import org.noear.water.utils.DisttimeUtils;
import waterapp.dso.db.DbWaterMsgApi;

import java.util.Date;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_msg_send extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //消息key //派发时会回传
        String topic = get("topic"); //订阅主题
        String message = get("message"); //消息内容
        String plan_time = get("plan_time"); //分发时间 //yyyy-MM-dd HH:mm:ss

        if (checkParamsIsOk(key, topic, message) == false) {
            return;
        }

        //如果不需要修改，检查是否已存在
        //
        if (DbWaterMsgApi.hasMessage(key)) {
            data.set("code", 1);
            data.set("msg", "success");
            return;
        }

        Date plan_time2 = DisttimeUtils.parse(plan_time);

        if (DbWaterMsgApi.addMessage(key, topic, message, plan_time2) > 0) {
            data.set("code", 1);
            data.set("msg", "success");
            return;
        }

        data.set("code", 0);
        data.set("msg", "unknown");
    }
}
