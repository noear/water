package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventBus;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息交换机（路由并入队）
 * */
@Component
public class MsgExchangeController implements IJob {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
        //保持队里只有2000数量
        if (ProtocolHub.messageQueue.count() > 2000) {
            return;
        }

        long ntime = System.currentTimeMillis();
        List<MessageModel> msgList = ProtocolHub.messageSource().getMessageListOfPending(1000, ntime);

        for (MessageModel msg : msgList) {
            //executor.submit(() -> {
                exchange(msg);
            //});
        }

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
            exchangeDo(msg);
        } catch (Throwable ex) {
            EventBus.push(ex);
        }
    }

    private void exchangeDo(MessageModel msg) {

        try {
            if (msg == null || msg.state == 1) { //如果找不到消息，或正在处理中
                return;
            }

            long ntime = DisttimeUtils.currTime();
            if (msg.dist_nexttime > ntime) { //如果时间还没到
                return;
            }

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

        //1.2.如果没有订阅者，就收工
        if (subsList.size() == 0) {
            ProtocolHub.messageSource().setMessageState(msg, MessageState.notarget);//-2);
            return;
        }

        //2.尝试建立分发关系
        if (msg.dist_routed == false) {
            for (SubscriberModel m : subsList.values()) {
                ProtocolHub.messageSource().addDistributionNoLock(msg, m);
            }

            ProtocolHub.messageSource().setMessageRouteState(msg, true);
        }

        String msg_id_str = String.valueOf(msg.msg_id);

        //if (ProtocolHub.messageLock.lock(msg_id_str)) {
        ProtocolHub.messageQueue.push(msg_id_str);
        //}

        //置为处理中
        ProtocolHub.messageSource().setMessageState(msg, MessageState.processed);//1);
    }
}
