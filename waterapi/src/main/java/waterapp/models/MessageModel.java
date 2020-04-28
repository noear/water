package waterapp.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/**
 * Created by noear on 2017/7/18.
 */
public class MessageModel implements IBinder {
    public long msg_id;
    public String msg_key;
    public int topic_id;
    public String content;
    public int state;
    public int dist_count;
    public long dist_nexttime;

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
        dist_nexttime = s.get("dist_nexttime").value(0l);
    }

    @Override
    public IBinder clone() {
        return new MessageModel();
    }
}
