package waterapp.controller;

import org.noear.solon.annotation.XBean;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.protocol.IMessageQueue;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import waterapp.dso.db.DbWaterMsgApi;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息入站（队列）
 * */
@XBean
public class MsgZanController implements IJob {
    @Override
    public String getName() {
        return "zan";
    }

    int _interval = 500;
    IMessageQueue _queue;

    @Override
    public int getInterval() {
        return _interval;
    }

    @Override
    public void exec() throws Exception {

        if (_queue == null) {
            _queue = ProtocolHub.messageQueue;
        }

//        保持队里只有200数量 //不再考虑数量
//        if (_queue.count() > 200) {
//            return;
//        }

        long ntime = System.currentTimeMillis();
        List<Long> msgList = DbWaterMsgApi.getMessageList(200, ntime);

        if (msgList.size() > 0) {
            _interval = 500;

            List<String> msgList2 = msgList.stream().map(m -> m + "").collect(Collectors.toList());
            _queue.pushAll(msgList2);
        } else {
            int hour = Datetime.Now().getHours();
            if (hour > 1 && hour < 6) {
                _interval = 2000;
            } else {
                _interval = 1000;
            }
        }
    }
}
