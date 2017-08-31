package watersev.controller;

import noear.snacks.ONode;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import watersev.dao.AlarmUtil;
import watersev.dao.DisttimeUtil;
import watersev.dao.LogUtil;
import watersev.dao.db.DbMsgApi;
import watersev.models.StateTag;
import watersev.models.water_msg.DistributionModel;
import watersev.models.water_msg.MessageModel;
import watersev.models.water_msg.SubscriberModel;
import watersev.utils.Base64Util;
import watersev.utils.EncryptUtil;
import watersev.utils.HttpUtil;
import watersev.utils.ext.Act3;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by yuety on 2017/7/18.
 */
public final class MsgController {
    private static final String tag = "MsgController";

    public static void main(int rows, int dTotal, int dIndex) throws SQLException {


        int ntime = DisttimeUtil.currTime();
        List<Long> msgList = DbMsgApi.getMessageList(rows, ntime);

        for (Long msgID : msgList) {
            if (dTotal > 0) {
                if (msgID % dTotal != dIndex) {
                    continue;
                }
            }

            MessageModel msg = DbMsgApi.getMessage(msgID);
            if (msg == null) {
                continue;
            }


            try {
                distribute(msg);
            } catch (Exception ex) {
                ex.printStackTrace();

                DbMsgApi.setMessageRepet(msg, 0); //如果失败，重新设为0 //重新操作一次

                LogUtil.error("distribute:" + msg.msg_id + "." + msg.topic_name, ex);
            }
        }
    }

    private static void distribute(MessageModel msg) throws SQLException {
        //1.取出订阅者
        List<SubscriberModel> subsList = DbMsgApi.getSubscriberListByTopic(msg.topic_id);

        //1.2.如果没有订阅者，就收工
        if(subsList.size()==0){
            DbMsgApi.setMessageState(msg.msg_id, -2);
            return;
        }

        //2.尝试建立分发关系
        for (SubscriberModel m : subsList) {
            DbMsgApi.addDistribution(msg.msg_id, m);
        }

        //3.获出待分发任务
        List<DistributionModel> distList = DbMsgApi.getDistributionListByMsg(msg.msg_id);

        //3.2.如果没有可派发对象，就收工
        if(distList.size()==0){
            DbMsgApi.setMessageState(msg.msg_id, 2);
            return;
        }

        //4.开始派发
        //
        StateTag state = new StateTag();
        state.msg = msg;
        state.total = distList.size();

        for (DistributionModel m : distList) {
            distributeMessage(state, msg, m, distributeMessage_callback);
        }
    }

    private static Act3<StateTag, DistributionModel, Boolean> distributeMessage_callback = (tag, dist, isOk) -> {
        tag.count += 1;
        if (isOk) {
            if (DbMsgApi.setDistributionState(tag.msg.msg_id, dist.dist_id, 2)) {
                tag.value += 1;
            }
        } else {
            DbMsgApi.setDistributionState(tag.msg.msg_id, dist.dist_id, 1);
        }

        //4.返回派发结果
        if (tag.count == tag.total) {
            if (tag.value == tag.total) {
                DbMsgApi.setMessageState(dist.msg_id, 2);

                if (tag.msg.dist_count >= 4) {
                    out.print("发送短信报警---\r\n");
                    AlarmUtil.tryAlarm(tag.msg, true, dist.alarm_mobile);
                }

            } else {
                DbMsgApi.setMessageRepet(tag.msg, 0);

                if (tag.msg.dist_count >= 4) {
                    out.print("发送短信报警---\r\n");
                    AlarmUtil.tryAlarm(tag.msg, false, dist.alarm_mobile);
                }
            }
        }
    };

    private static void distributeMessage(StateTag tag, MessageModel msg, DistributionModel dist, Act3<StateTag, DistributionModel, Boolean> callback) {

        //1.生成签名
        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.msg_id).append("#");
        sb.append(msg.msg_key).append("#");
        sb.append(msg.topic_name).append("#");
        sb.append(msg.content).append("#");
        sb.append(dist.access_key);

        String sgin = EncryptUtil.md5(sb.toString());

        //2.组装分源的数据
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", msg.msg_id + ""));
        params.add(new BasicNameValuePair("key", msg.msg_key));
        params.add(new BasicNameValuePair("topic", msg.topic_name));
        params.add(new BasicNameValuePair("message", Base64Util.encode(msg.content)));
        params.add(new BasicNameValuePair("sgin", sgin));


        try {
            if (dist.is_sync == 1) {
                //3.1.进行同步http分发
                String rst = HttpUtil.postString(dist.receive_url, params);

                LogUtil.writeForMsg("distributeMessage", msg, rst);

                callback.run(tag, dist, "OK".equals(rst));
            } else {
                //3.2.进行异步http分发
                HttpUtil.postStringByAsync(dist.receive_url, params, (isOk, code, rst) -> {
                    if (isOk) {
                        boolean isOk2 = "OK".equals(rst);

                        LogUtil.writeForMsg("distributeMessage", msg, rst);

                        if(isOk2 == false){
                            //同时在错误的书写器里，写入一条
                            LogUtil.writeForMsgByError("distributeMessage", msg, rst);
                        }

                        callback.run(tag, dist, isOk2);
                    } else {
                        LogUtil.writeForMsg("distributeMessage", msg, "http error");

                        //同时在错误的书写器里，写入一条
                        LogUtil.writeForMsgByError("distributeMessage", msg, "http error");

                        callback.run(tag, dist, false);
                    }
                });
            }

        } catch (Exception ex) {
            LogUtil.writeForMsg("distributeMessage", msg, ex.getLocalizedMessage());

            //同时在错误的书写器里，写入一条
            LogUtil.writeForMsgByError("distributeMessage", msg, ex.getLocalizedMessage());

            callback.run(tag, dist, false);
        }
    }
}
