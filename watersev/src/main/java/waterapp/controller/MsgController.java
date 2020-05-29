package waterapp.controller;

import org.noear.solon.XApp;
import org.noear.solon.annotation.XBean;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.protocol.IMessageQueue;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.*;
import waterapp.dso.AlarmUtil;
import waterapp.dso.LogUtil;
import waterapp.dso.db.DbWaterMsgApi;
import waterapp.models.StateTag;
import waterapp.models.water_msg.DistributionModel;
import waterapp.models.water_msg.MessageModel;
import waterapp.models.water_msg.SubscriberModel;
import waterapp.utils.ext.Act3;

import java.sql.SQLException;
import java.util.*;

import static java.lang.System.out;

/**
 * 消息派发
 * */
@XBean
public final class MsgController implements IJob {

    private int _threads;
    public MsgController(){
        _threads = XApp.cfg().argx().getInt("pool");

        if(_threads < 1){
            _threads = 1;
        }
    }

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public int getInterval() {
        return 10;
    }

    @Override
    public int getThreads() {
        return _threads;
    }

    IMessageQueue _queue;

    @Override
    public void exec() throws Exception {

        if (_queue == null) {
            _queue = ProtocolHub.messageQueue;
        }

        while (true) {
            String msg_id_str = _queue.pop();

            if (TextUtils.isEmpty(msg_id_str)) {//如果没有了
                break;
            }

            long msgID = Long.parseLong(msg_id_str);

            MessageModel msg = DbWaterMsgApi.getMessage(msgID);
            if (msg == null) { //如果找不到消息
                continue;
            }

            long ntime = DisttimeUtils.currTime();
            if (msg.dist_nexttime > ntime) { //如果时间还没到
                continue;
            }

            //置为处理中
            DbWaterMsgApi.setMessageState(msgID, 1);
            //并将消息锁里取消掉
            _queue.remove(msg_id_str);

            try {
                distribute(msg);
            } catch (Exception ex) {
                ex.printStackTrace();

                DbWaterMsgApi.setMessageRepet(msg, 0); //如果失败，重新设为0 //重新操作一次

                LogUtil.writeForMsgByError(msg, ex);
            }
        }
    }

    //===============


    private void distribute(MessageModel msg) throws SQLException {
        //1.取出订阅者
        Map<Integer,SubscriberModel> subsList = DbWaterMsgApi.getSubscriberListByTopic(msg.topic_id);

        //1.2.如果没有订阅者，就收工
        if(subsList.size()==0){
            DbWaterMsgApi.setMessageState(msg.msg_id, -2);
            return;
        }

        //2.尝试建立分发关系
        for (SubscriberModel m : subsList.values()) {
            DbWaterMsgApi.addDistribution(msg.msg_id, m);
        }

        //3.获出待分发任务
        List<DistributionModel> distList = new ArrayList<>();
        List<DistributionModel> distList_tmp = DbWaterMsgApi.getDistributionListByMsg(msg.msg_id);

        //3.1.过滤可能已不存在的订阅者
        for(DistributionModel d : distList_tmp) { //可能会有什么意外，会产生重复数据
            SubscriberModel s1 = subsList.get(d.subscriber_id);
            if (s1 != null) {
                d._is_unstable = s1.is_unstable;
                distList.add(d);
            }
        }

        //3.2.如果没有可派发对象，就收工
        if(distList.size()==0){
            DbWaterMsgApi.setMessageState(msg.msg_id, 2);
            return;
        }

        //4.开始派发
        //
        StateTag state = new StateTag();
        state.msg = msg;
        state.total = distList.size();

        for (DistributionModel m : distList) {
            m._start_time = new Date();

            distributeMessage(state, msg, m, distributeMessage_callback);
        }
    }

    private  Act3<StateTag, DistributionModel, Boolean> distributeMessage_callback = (tag, dist, isOk) -> {
        tag.count += 1;
        if (isOk) {
            if (DbWaterMsgApi.setDistributionState(tag.msg.msg_id, dist, 2)) {
                tag.value += 1;
            }
        } else {
            DbWaterMsgApi.setDistributionState(tag.msg.msg_id, dist, 1);
        }

        //4.返回派发结果
        if (tag.count == tag.total) {
            if (tag.value == tag.total) {
                DbWaterMsgApi.setMessageState(dist.msg_id, 2);

                if (tag.msg.dist_count >= 4) {
                    out.print("发送短信报警---\r\n");
                    AlarmUtil.tryAlarm(tag.msg, true, dist);
                }

            } else {
                if(tag.msg.isDistributionEnd()) { //是否已派发结束（超出超大派发次数）
                    DbWaterMsgApi.setMessageRepet(tag.msg, 3);

                    out.print("发送短信报警---\r\n");
                    AlarmUtil.tryAlarm(tag.msg, false, dist);
                }
                else {
                    DbWaterMsgApi.setMessageRepet(tag.msg, 0);

                    if (tag.msg.dist_count >= 4) {
                        out.print("发送短信报警---\r\n");
                        AlarmUtil.tryAlarm(tag.msg, false, dist);
                    }
                }
            }
        }
    };

    private  void distributeMessage(StateTag tag, MessageModel msg, DistributionModel dist, Act3<StateTag, DistributionModel, Boolean> callback) {

        //1.生成签名
        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.msg_id).append("#");
        sb.append(msg.msg_key).append("#");
        sb.append(msg.topic_name).append("#");
        sb.append(msg.content).append("#");
        sb.append(dist.access_key);

        String sgin = EncryptUtils.md5(sb.toString());

        //2.组装分源的数据
        Map<String,String> params = new HashMap<>();
        params.put("id", msg.msg_id + "");
        params.put("key", msg.msg_key);
        params.put("topic", msg.topic_name);
        params.put("times", msg.dist_count + "");
        params.put("message", Base64Utils.encode(msg.content));
        params.put("sgin", sgin);


        try {
            if(dist.receive_way == 0){
                //3.2.0.进行异步http分发
                HttpUtils.http(dist.receive_url).data(params).postAsync((isOk, resp, ex) -> {

                    dist._duration = new Timespan(dist._start_time).seconds();

                    if (isOk) {
                        String rst = resp.body().string();

                        boolean isOk2 = "OK".equals(rst);

                        LogUtil.writeForMsg(msg, dist, rst);

                        if (isOk2 == false) {
                            //同时在错误的书写器里，写入一条
                            LogUtil.writeForMsgByError(msg, dist, rst);
                        }

                        callback.run(tag, dist, isOk2);
                    } else {
                        LogUtil.writeForMsg(msg, dist, "http error");

                        //同时在错误的书写器里，写入一条
                        LogUtil.writeForMsgByError( msg, dist,"http error");

                        callback.run(tag, dist, false);
                    }
                });
            }

            if(dist.receive_way == 2){
                //3.2.2.进行异步http分发 //不等待 //状态设为已完成
                HttpUtils.http(dist.receive_url).data(params).postAsync((isOk, resp, ex) -> {

                    dist._duration = new Timespan(dist._start_time).seconds();

                    if (isOk) {
                        String rst = resp.body().string();

                        boolean isOk2 = "OK".equals(rst);

                        LogUtil.writeForMsg( msg, dist, rst);

                        if (isOk2 == false) {
                            //同时在错误的书写器里，写入一条
                            LogUtil.writeForMsgByError( msg, dist, rst);
                        }


                    } else {
                        LogUtil.writeForMsg( msg, dist, "http error");

                        //同时在错误的书写器里，写入一条
                        LogUtil.writeForMsgByError( msg, dist,"http error");
                    }
                });

                callback.run(tag, dist, true);
            }

            if(dist.receive_way == 3){
                //3.2.3.进行异步http分发 //不等待 //状态设为处理中（等消费者主动设为成功）
                HttpUtils.http(dist.receive_url).data(params).postAsync((isOk, resp, ex) -> {

                    dist._duration = new Timespan(dist._start_time).seconds();

                    if (isOk) {
                        String rst = resp.body().string();

                        boolean isOk2 = "OK".equals(rst);

                        LogUtil.writeForMsg( msg, dist, rst);

                        if (isOk2 == false) {
                            //同时在错误的书写器里，写入一条
                            LogUtil.writeForMsgByError( msg, dist, rst);
                        }


                    } else {
                        LogUtil.writeForMsg( msg, dist, "http error");

                        //同时在错误的书写器里，写入一条
                        LogUtil.writeForMsgByError(msg, dist,"http error");
                    }
                });

                //推后一小时，可手工再恢复
                long ntime = DisttimeUtils.nextTime(Datetime.Now().addHour(1).getFulltime());
                DbWaterMsgApi.setMessageState(msg.msg_id, 1, ntime);
            }

        } catch (Exception ex) {
            LogUtil.writeForMsg( msg, dist, ex.getLocalizedMessage());

            //同时在错误的书写器里，写入一条
            LogUtil.writeForMsgByError( msg, dist,ex.getLocalizedMessage());

            callback.run(tag, dist, false);
        }
    }
}
