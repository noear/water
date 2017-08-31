package waterapi.controller.cmds;

import waterapi.dao.db.DbMsgApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_msg_subscribe extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //订阅者唯一key
        String alarm_mobile = get("alarm_mobile");//订阅者的报敬接收手机号

        String topic = get("topic"); //订阅主题 //多个主题以","隔开
        String receive_url = get("receiver_url");//接收者url
        String access_key = get("access_key");//接收者url
        String is_sync = get("sync"); //是否同步形式派发


        if (checkParamsIsOk(key, topic, receive_url, access_key) == false) {
            return;
        }

        boolean is_sync2 = "1".equals(is_sync);


        boolean isOk = true;
        for (String topic2 : topic.split(",")) {//多个主题以","隔开
            if (topic2.length() > 0) {
                isOk = isOk & do_subscprebe(key, alarm_mobile,topic2, receive_url, access_key, is_sync2);
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

    private boolean do_subscprebe(String key,String alarm_mobile, String topic, String receive_url, String access_key, boolean is_sync) throws Exception {
        if (DbMsgApi.hasSubscriber(key, topic)) {
            DbMsgApi.udpSubscriber(key,alarm_mobile, topic, receive_url, access_key, is_sync);
            return true;
        }

        if (DbMsgApi.addSubscriber(key, alarm_mobile,topic, receive_url, access_key, is_sync) > 0) {
            return true;
        }

        return false;
    }
}
