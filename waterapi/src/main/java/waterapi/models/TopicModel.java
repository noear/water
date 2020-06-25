package waterapi.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

public class TopicModel implements IBinder {
    public int topic_id;
    public int max_msg_num;
    public int model;

    @Override
    public void bind(GetHandlerEx s) {
        topic_id = s.get("topic_id").value(0);
        max_msg_num = s.get("max_msg_num").value(0);
        model = s.get("model").value(0);
    }

    @Override
    public IBinder clone() {
        return new TopicModel();
    }
}
