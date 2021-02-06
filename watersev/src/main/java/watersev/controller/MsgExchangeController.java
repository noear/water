package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.DisttimeUtils;
import watersev.dso.LogUtil;
import watersev.dso.db.DbWaterMsgApi;

import java.util.List;
import java.util.Map;

/**
 * 消息交换机（路由并入队）
 * */
@Component
public class MsgExchangeController implements IJob {
    @Override
    public String getName() {
        return "zan";
    }

    int _interval_def = 100;
    int _interval = 100;

    @Override
    public int getInterval() {
        return _interval;
    }

    @Override
    public void exec() throws Exception {
        long dist_nexttime = System.currentTimeMillis();
        List<MessageModel> msgList = ProtocolHub.messageSource()
                .getMessageListOfPending(1000, dist_nexttime);

        msgList.parallelStream().forEachOrdered((msg) -> {
            exchange(msg);
        });

        if (msgList.size() > 0) {
            _interval = _interval_def;
        } else {
            int hour = Datetime.Now().getHours();

            if (hour > 1 && hour < 6) {
                _interval = 2000;
            } else {
                _interval = 500;
            }
        }
    }

    private void exchange(MessageModel msg) {
        try {
            long ntime = DisttimeUtils.currTime();
            if (msg.dist_nexttime > ntime) { //如果时间还没到
                return;
            }

            exchangeDo(msg);
        } catch (Throwable ex) {
            LogUtil.writeForMsgByError(msg, ex);
        }
    }

    private void exchangeDo(MessageModel msg) {
        try {

            routing(msg);

        } catch (Throwable ex) {
            if (msg != null) {
                ProtocolHub.messageSource().setMessageRepet(msg, MessageState.undefined);//0); //如果失败，重新设为0 //重新操作一次

                LogUtil.writeForMsgByError(msg, ex);
            }
        }
    }

    private void routing(MessageModel msg) throws Exception {
        //1.取出订阅者
        Map<Integer, SubscriberModel> subsList = DbWaterMsgApi.getSubscriberListByTopic(msg.topic_name);

        //2.如果没有订阅者，就收工
        if (subsList.size() == 0) {
            ProtocolHub.messageSource().setMessageState(msg, MessageState.notarget);//-2);
            return;
        }

        //3.尝试建立路由关系和派发任务
        if (msg.dist_routed == false) {
            for (SubscriberModel m : subsList.values()) {
                ProtocolHub.messageSource().addDistributionNoLock(msg, m);
            }

            ProtocolHub.messageSource().setMessageRouteState(msg, true);
        }

        //4.推送隐列
        String msg_id_str = String.valueOf(msg.msg_id);

        ProtocolHub.messageQueue.push(msg_id_str);

        //5.状态改为处理中
        ProtocolHub.messageSource().setMessageState(msg, MessageState.processed);
    }
}
