package watersev.controller;

import org.noear.solon.annotation.XBean;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import watersev.dso.db.DbWaterMsgApi;

import java.util.List;

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

    @Override
    public int getInterval() {
        return _interval;
    }

    @Override
    public void exec() throws Exception {
        //保持队里只有1000数量
        if (ProtocolHub.messageQueue.count() > 1000) {
            return;
        }

        long ntime = System.currentTimeMillis();
        List<Long> msgList = DbWaterMsgApi.getMessageList(200, ntime);

        if (msgList.size() > 0) {
            _interval = 500;

            for (Long msg_id : msgList) {
                String msg_id_str = msg_id + "";
                if (ProtocolHub.messageLock.lock(msg_id_str)) {
                    ProtocolHub.messageQueue.push(msg_id_str);
                }
            }
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
