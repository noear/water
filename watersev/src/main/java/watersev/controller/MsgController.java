package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.utils.*;
import watersev.Config;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterMsgApi;
import watersev.models.StateTag;
import watersev.models.water_msg.DistributionModel;
import watersev.models.water_msg.MessageModel;
import watersev.models.water_msg.SubscriberModel;
import watersev.utils.ext.Act3;

import java.sql.SQLException;
import java.util.*;

/**
 * 消息派发
 *
 * 订阅类型（0,1异步等待 ; 2异步不等待并设为成功 ; 3异步不待等并设为处理中 ;）
 * 消息状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
 * */
@Component
public final class MsgController implements IJob {

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public int getInterval() {
        return 10; //让CPU稍微休息下
    }

    @Override
    public void exec() throws Exception {
        ProtocolHub.messageQueue.pollGet(msg_id_str -> {
            if (TextUtils.isEmpty(msg_id_str)) {
                //说明没有了
                return;
            }

            //改用线程池处理
            Config.pools.execute(() -> exchange(msg_id_str));
        });
    }

    public void unlock(String key){
        ProtocolHub.messageLock.unlock(key);
    }

    public boolean lock(String key){
        return ProtocolHub.messageLock.lock(key);
    }

    private void exchange(String msg_id_str) {
        try {
            exchangeDo(msg_id_str);
        } catch (Throwable ex) {
            EventBus.push(ex);
        } finally {
            ContextUtil.currentRemove();
        }
    }

    private void exchangeDo(String msg_id_str) throws Exception {
        String lk_msg_id_do = msg_id_str + "_do";

        if (lock(lk_msg_id_do) == false) {
            return;
        }

        long msgID = Long.parseLong(msg_id_str);
        MessageModel msg = null;

        try {
            msg = DbWaterMsgApi.getMessageOfPending(msgID); //可能会出异常

            if (msg == null || msg.state == 1) { //如果找不到消息，或正在处理中
                unlock(lk_msg_id_do);
                return;
            }

            long ntime = DisttimeUtils.currTime();
            if (msg.dist_nexttime > ntime) { //如果时间还没到
                unlock(lk_msg_id_do);
                return;
            }

            //记录锁key
            msg.lk_msg_id_do = lk_msg_id_do;

            //事务相关性设定
            ContextUtil.currentSet(new ContextEmpty());
            ContextUtil.current().headerSet(WW.http_header_trace, msg.trace_id);

            //置为处理中
            DbWaterMsgApi.setMessageState(msg, MessageState.processed);//1);

            routing(msg);

        } catch (Throwable ex) {
            if (msg != null) {
                DbWaterMsgApi.setMessageRepet(msg, MessageState.undefined);//0); //如果失败，重新设为0 //重新操作一次

                LogUtil.writeForMsgByError(msg, ex);
            }

            //如果异常了，及时解锁（如果成功，在回调里解锁）
            unlock(lk_msg_id_do);
        }
    }


    private void routing(MessageModel msg) throws SQLException {
        //1.取出订阅者
        Map<Integer, SubscriberModel> subsList = DbWaterMsgApi.getSubscriberListByTopic(msg.topic_id);

        //1.2.如果没有订阅者，就收工
        if (subsList.size() == 0) {
            DbWaterMsgApi.setMessageState(msg, MessageState.notarget);//-2);
            unlock(msg.lk_msg_id_do);
            return;
        }

        //2.尝试建立分发关系
        if(msg.dist_routed == false) {
            for (SubscriberModel m : subsList.values()) {
                DbWaterMsgApi.addDistributionNoLock(msg, m);
            }

            DbWaterMsgApi.setMessageRouteState(msg,true);
        }

        //3.获出待分发任务
        List<DistributionModel> distList = new ArrayList<>();
        List<DistributionModel> distList_tmp = DbWaterMsgApi.getDistributionListByMsg(msg.msg_id);

        //3.1.过滤可能已不存在的订阅者
        for (DistributionModel d : distList_tmp) { //可能会有什么意外，会产生重复数据
            SubscriberModel s1 = subsList.get(d.subscriber_id);
            if (s1 != null) {
                d._is_unstable = s1.is_unstable;
                distList.add(d);
            }
        }

        //3.2.如果没有可派发对象，就收工
        if (distList.size() == 0) {
            DbWaterMsgApi.setMessageState(msg, MessageState.completed);//2);
            unlock(msg.lk_msg_id_do);
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

    private Act3<StateTag, DistributionModel, Boolean> distributeMessage_callback = (tag, dist, isOk) -> {
        synchronized (tag.msg.msg_id) {
            //
            //锁一下，确保计数的线程安全
            //
            tag.count += 1;
            if (isOk) {
                if (DbWaterMsgApi.setDistributionState(tag.msg, dist, MessageState.completed)) {//2
                    tag.value += 1;
                }
            } else {
                DbWaterMsgApi.setDistributionState(tag.msg, dist, MessageState.processed);//1);
            }

            //4.返回派发结果
            if (tag.count == tag.total) {
                //处理完了后，解锁
                ProtocolHub.messageLock.unlock(tag.msg.lk_msg_id_do);

                if (tag.value == tag.total) {
                    DbWaterMsgApi.setMessageState(tag.msg, MessageState.completed);//2);

                    if (tag.msg.dist_count >= 3) {
//                    System.out.print("发送短信报警---\r\n");
                        AlarmUtil.tryAlarm(tag.msg, true, dist);
                    }

                } else {
                    if (tag.msg.isDistributionEnd()) { //是否已派发结束（超出超大派发次数）
                        DbWaterMsgApi.setMessageRepet(tag.msg, MessageState.excessive);//3);

//                    System.out.print("发送短信报警---\r\n");
                        AlarmUtil.tryAlarm(tag.msg, false, dist);
                    } else {
                        DbWaterMsgApi.setMessageRepet(tag.msg, MessageState.undefined);//0);

                        if (tag.msg.dist_count >= 3) {
//                        System.out.print("发送短信报警---\r\n");
                            AlarmUtil.tryAlarm(tag.msg, false, dist);
                        }
                    }
                }
            }
        }
    };

    private void distributeMessage(StateTag tag, MessageModel msg, DistributionModel dist, Act3<StateTag, DistributionModel, Boolean> callback) {

        //1.生成签名
        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.msg_id).append("#");
        sb.append(msg.msg_key).append("#");
        sb.append(msg.topic_name).append("#");
        sb.append(msg.content).append("#");
        sb.append(dist.receive_key);

        String sgin = EncryptUtils.md5(sb.toString());

        //2.组装分源的数据
        Map<String, String> params = new HashMap<>();
        params.put("id", msg.msg_id + "");
        params.put("key", msg.msg_key);
        params.put("topic", msg.topic_name);
        params.put("times", msg.dist_count + "");
        params.put("trace_id", msg.trace_id);
        params.put("message", Base64Utils.encode(msg.content));
        params.put("tags", msg.tags);
        params.put("sgin", sgin);


        try {
            if (dist.receive_way == 2) {
                //::2
                //
                //3.2.2.进行异步http分发 //不等待 //状态设为已完成
                HttpUtils.http(dist.receive_url)
                        .header(WW.http_header_trace, msg.trace_id)
                        .data(params).postAsync((isOk, resp, ex) -> {

                    dist._duration = new Timespan(dist._start_time).milliseconds();

                    if (isOk) {
                        String rst = resp.body().string();

                        boolean isOk2 = "OK".equals(rst);


                        if (isOk2 == false) {
                            LogUtil.writeForMsg(msg, dist, rst);
                        } else {
                            LogUtil.writeForMsgByError(msg, dist, rst);
                        }
                    } else {
                        LogUtil.writeForMsgByError(msg, dist, "http error");
                    }
                });

                callback.run(tag, dist, true);
            } else if (dist.receive_way == 3) {
                //::3
                //
                //3.2.3.进行异步http分发 //不等待 //状态设为处理中（等消费者主动设为成功）
                HttpUtils.http(dist.receive_url)
                        .header(WW.http_header_trace, msg.trace_id)
                        .data(params).postAsync((isOk, resp, ex) -> {

                    dist._duration = new Timespan(dist._start_time).milliseconds();

                    if (isOk) {
                        String rst = resp.body().string();

                        boolean isOk2 = "OK".equals(rst);

                        if (isOk2) {
                            LogUtil.writeForMsg(msg, dist, rst);
                        } else {
                            //同时在错误的书写器里，写入一条
                            LogUtil.writeForMsgByError(msg, dist, rst);
                        }
                    } else {
                        LogUtil.writeForMsgByError(msg, dist, "http error");
                    }
                });

                //推后一小时，可手工再恢复
                long ntime = DisttimeUtils.distTime(Datetime.Now().addHour(1).getFulltime());
                DbWaterMsgApi.setMessageState(msg, MessageState.processed, ntime);//1
            } else {
                //::0,1
                //
                //3.2.0.进行异步http分发
                HttpUtils.http(dist.receive_url)
                        .header(WW.http_header_trace, msg.trace_id)
                        .data(params).postAsync((isOk, resp, ex) -> {

                    dist._duration = new Timespan(dist._start_time).milliseconds();

                    if (isOk) {
                        String rst = resp.body().string();

                        boolean isOk2 = "OK".equals(rst);

                        if (isOk2) {
                            LogUtil.writeForMsg(msg, dist, rst);
                        } else {
                            LogUtil.writeForMsgByError(msg, dist, rst);
                        }

                        callback.run(tag, dist, isOk2);
                    } else {
                        LogUtil.writeForMsgByError(msg, dist, "http error");

                        callback.run(tag, dist, false);
                    }
                });
            }

        } catch (Exception ex) {
            LogUtil.writeForMsgByError(msg, dist, ex.getLocalizedMessage());

            callback.run(tag, dist, false);
        }
    }
}
