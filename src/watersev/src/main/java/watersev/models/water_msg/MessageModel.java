package watersev.models.water_msg;

import noear.snacks.ONode;
import noear.weed.GetHandlerEx;
import noear.weed.IBinder;

/**
 * Created by yuety on 2017/7/18.
 */
public class MessageModel implements IBinder {
    public long msg_id;
    public String msg_key;
    public int topic_id;
    public String content;
    public int state;
    public int dist_count;
    public int dist_nexttime;

    public String topic_name;

    @Override
    public void bind(GetHandlerEx s) {
        msg_id = s.get("msg_id").value(0l);
        msg_key = s.get("msg_key").value("");

        topic_id = s.get("topic_id").value(0);
        topic_name = s.get("topic_name").value("");

        content = s.get("content").value("");
        state = s.get("state").value(0);
        dist_count = s.get("dist_count").value(0);
        dist_nexttime = s.get("dist_nexttime").value(0);
    }

    @Override
    public IBinder clone() {
        return new MessageModel();
    }
}
