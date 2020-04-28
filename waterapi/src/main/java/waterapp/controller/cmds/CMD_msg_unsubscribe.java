package waterapp.controller.cmds;

import waterapp.dso.db.DbWaterMsgApi;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_msg_unsubscribe extends CMDBase {

    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //订阅者唯一key
        String topic = get("topic"); //订阅主题 //多个主题以","隔开

        if (checkParamsIsOk(key, topic) == false) {
            return;
        }

        boolean isOk = true;
        for (String topic2 : topic.split(",")) {//多个主题以","隔开
            if (topic2.length() > 0) {
                isOk = isOk & do_unsubscprebe(key, topic2);
            }
        }


        if(isOk) {
            data.set("code", 1);
            data.set("msg", "success");
        }else{
            data.set("code",0);
            data.set("msg","unknown");
        }
    }

    private boolean do_unsubscprebe(String key, String topic) throws Exception {
        return DbWaterMsgApi.removeSubscriber(key, topic);
    }
}
