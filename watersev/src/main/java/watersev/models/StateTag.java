package watersev.models;


import org.noear.water.protocol.model.message.MessageModel;
import watersev.dso.db.DbWaterMsgApi;
import watersev.models.water_msg.TopicModel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by noear on 2017/7/18.
 */
public final class StateTag {
    public final MessageModel msg;

    public final int total;//总数
    public final AtomicInteger count = new AtomicInteger(0);//计数
    public final AtomicInteger value = new AtomicInteger(0);//有效计数

    public StateTag(MessageModel msg, int total){
        this.msg = msg;
        this.total = total;
    }


    private TopicModel _topic;

    public TopicModel topic() {
        if (_topic == null) {
            try {
                _topic = DbWaterMsgApi.getTopic(msg.topic_name);
            } catch (Exception ex) {
                ex.printStackTrace();
                _topic = new TopicModel();
            }
        }

        return _topic;
    }

    private int _max_dist_num = -1;

    public boolean isDistributionEnd() {
        if (_max_dist_num < 0) {
            _max_dist_num = topic().max_distribution_num;
        }

        return (_max_dist_num > 0 && msg.dist_count >= _max_dist_num);
    }
}
