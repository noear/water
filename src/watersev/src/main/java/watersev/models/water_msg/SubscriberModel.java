package watersev.models.water_msg;

import noear.weed.GetHandlerEx;
import noear.weed.IBinder;

/**
 * Created by yuety on 2017/7/18.
 */
public class SubscriberModel implements IBinder {
    public int subscriber_id;
    public String alarm_mobile;
    public int topic_id;
    public String receive_url;
    public String access_key;
    public int is_sync;

    @Override
    public void bind(GetHandlerEx s) {
        subscriber_id = s.get("subscriber_id").value(0);
        alarm_mobile= s.get("alarm_mobile").value("");
        topic_id = s.get("topic_id").value(0);
        receive_url = s.get("receive_url").value("");
        access_key = s.get("access_key").value("");
        is_sync = s.get("is_sync").value(0);
    }

    @Override
    public IBinder clone() {
        return new SubscriberModel();
    }
}
