package watersev.models.water_msg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/**
 * Created by noear on 2017/7/18.
 */
public class SubscriberModel implements IBinder {
    public int subscriber_id;
    public String subscriber_key;

    public String subscriber_note;
    public String alarm_mobile;
    public String alarm_sign;

    public int topic_id;
    public String receive_url;
    public int    receive_way;
    public String access_key;

    public int check_last_state;
    public int check_error_num;

    public boolean is_enabled;
    public boolean is_unstable;

    @Override
    public void bind(GetHandlerEx s) {
        subscriber_id = s.get("subscriber_id").value(0);
        subscriber_key = s.get("subscriber_key").value("");

        subscriber_note = s.get("subscriber_note").value("");

        alarm_mobile= s.get("alarm_mobile").value("");
        alarm_sign   = s.get("alarm_sign").value("");

        topic_id = s.get("topic_id").value(0);
        receive_url = s.get("receive_url").value("");
        access_key = s.get("access_key").value("");
        receive_way = s.get("receive_way").value(0);

        check_last_state = s.get("check_last_state").value(0);
        check_error_num = s.get("check_error_num").value(0);

        is_enabled = s.get("is_enabled").intValue(0) > 0;
        is_unstable = s.get("is_unstable").intValue(0) > 0;
    }

    @Override
    public IBinder clone() {
        return new SubscriberModel();
    }
}
