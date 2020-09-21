package watersev.models.water_msg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import watersev.dso.db.DbWaterMsgApi;

/**
 * Created by noear on 2017/7/18.
 */
public class MessageModel implements IBinder {
    public Long msg_id;
    public String msg_key;
    public String trace_id;
    public int topic_id;
    public String topic_name;
    public String content;
    public int dist_count;
    public long dist_nexttime;

    public String tags;

    //状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
    public int state;

    public String lk_msg_id_do;

    @Override
    public void bind(GetHandlerEx s) {
        msg_id = s.get("msg_id").value(0L);
        msg_key = s.get("msg_key").value("");

        trace_id = s.get("trace_id").value("");

        topic_id = s.get("topic_id").value(0);
        topic_name = s.get("topic_name").value("");

        content = s.get("content").value("");
        tags = s.get("tags").value("");

        state = s.get("state").value(0);
        dist_count = s.get("dist_count").value(0);
        dist_nexttime = s.get("dist_nexttime").value(0L);
    }

    @Override
    public IBinder clone() {
        return new MessageModel();
    }

    private  TopicModel _topic;
    public TopicModel topic(){
        if(_topic==null) {
            try {
                _topic = DbWaterMsgApi.getTopic(topic_id);
            }catch (Exception ex){
                ex.printStackTrace();
                _topic = new TopicModel();
            }
        }

        return _topic;
    }

    private int _max_dist_num=-1;
    public boolean isDistributionEnd() {
        if (_max_dist_num < 0) {
            _max_dist_num = topic().max_distribution_num;
        }

        return (_max_dist_num > 0 && dist_count >= _max_dist_num);
    }
}
