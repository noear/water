package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.utils.DisttimeUtils;
import watersev.dso.LogUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息交换器（从持久层转入队列）
 *
 * @author noear
 * */
@Component
public class MsgExchangeController implements IJob {
    static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

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
        while (true) {
            if (execDo() == false) {
                break;
            }
        }
    }

    private boolean execDo() throws Exception {
        if (ProtocolHub.messageQueue.count() > 20000) {
            //
            //防止派发机出问题时，队列不会暴掉
            //
            return false;
        }

        long dist_nexttime = System.currentTimeMillis();
        List<MessageModel> msgList = ProtocolHub.messageSource()
                .getMessageListOfPending(5000, dist_nexttime);

        CountDownLatch countDownLatch = new CountDownLatch(msgList.size());

        for (MessageModel msg : msgList) {
            executor.execute(() -> {
                exchange(msg);
                countDownLatch.countDown();
            });
        }

        //等待执行完成，再到下一轮
        countDownLatch.await();

        if (msgList.size() > 0) {
            _interval = _interval_def;
            return true;
        } else {
            _interval = 500;
            return false;
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
            //1.推送到队列（未来可以转发到不同的队列）
            String msg_id_str = String.valueOf(msg.msg_id);

            ProtocolHub.messageQueue.push(msg_id_str);

            //2.状态改为处理中
            ProtocolHub.messageSource().setMessageState(msg, MessageState.processed);

        } catch (Throwable ex) {
            ProtocolHub.messageSource()
                    .setMessageState(msg, MessageState.undefined);//0); //如果失败，重新设为0 //重新操作一次

            LogUtil.writeForMsgByError(msg, ex);
        }
    }
}
