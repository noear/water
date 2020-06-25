package waterapi.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/**
 * Created by noear on 2017/7/18.
 */
public class SubscriberModel implements IBinder {
    public int subscriber_id;

    public String alarm_mobile;
    public String alarm_sign;

    public int topic_id;
    public String receive_url;
    public int    receive_way;
    public String access_key;


    @Override
    public void bind(GetHandlerEx s) {
        subscriber_id = s.get("subscriber_id").value(0);

        alarm_mobile= s.get("alarm_mobile").value("");
        alarm_sign   = s.get("alarm_sign").value("");

        topic_id = s.get("topic_id").value(0);
        receive_url = s.get("receive_url").value("");
        access_key = s.get("access_key").value("");
        receive_way = s.get("receive_way").value(0);
    }

    @Override
    public IBinder clone() {
        return new SubscriberModel();
    }
}
