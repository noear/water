package waterapi.controller.cmds;

import waterapi.dso.db.DbWaterMsgApi;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_msg_subscribe extends CMDBase {

    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key"); //订阅者唯一key
        String note = get("note"); //订阅者描述
        String alarm_mobile = get("alarm_mobile", "");//订阅者的报敬接收手机号
        int is_unstable = getInt("is_unstable", 0);

        String topic = get("topic"); //订阅主题 //多个主题以","隔开
        String receive_url = get("receiver_url");//接收者url
        int    receive_way = getInt("receive_way");//接收方式//0,1,2
        String access_key = get("access_key");//接收者url

        if (checkParamsIsOk(key, topic, receive_url, access_key) == false) {
            return;
        }

        boolean isOk = true;
        for (String topic2 : topic.split(",")) {//多个主题以","隔开
            if (topic2.length() > 0) {
                isOk = isOk & do_subscprebe(key, note, alarm_mobile,topic2, receive_url, access_key, receive_way, is_unstable > 0);
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

    private boolean do_subscprebe(String key,String note,String alarm_mobile, String topic, String receive_url, String access_key, int receive_way, boolean is_unstable) throws Exception {
        if(receive_url.indexOf("://") < 0){ //如果不是url不能订阅
            return false;
        }

        if (DbWaterMsgApi.hasSubscriber(key, topic)) {
            DbWaterMsgApi.udpSubscriber(key, note, alarm_mobile, topic, receive_url, access_key, receive_way, is_unstable);
            return true;
        }

        if (DbWaterMsgApi.addSubscriber(key, note, alarm_mobile,topic, receive_url, access_key, receive_way, is_unstable) > 0) {
            return true;
        }

        return false;
    }
}
