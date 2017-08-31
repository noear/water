package watersev.models.water_msg;

import noear.weed.GetHandlerEx;
import noear.weed.IBinder;

/**
 * Created by yuety on 2017/7/18.
 */
public class DistributionModel implements IBinder {
    public long dist_id;
    public long msg_id;
    public int subscriber_id;
    public String alarm_mobile;
    public String receive_url;
    public String access_key;

    public int is_sync;
    public int state;

    @Override
    public void bind(GetHandlerEx s) {
        dist_id = s.get("dist_id").value(0l);
        msg_id = s.get("msg_id").value(0l);
        subscriber_id = s.get("subscriber_id").value(0);
        alarm_mobile = s.get("alarm_mobile").value("");
        receive_url = s.get("receive_url").value("");
        access_key = s.get("access_key").value("");
        is_sync = s.get("is_sync").value(0);
        state = s.get("state").value(0);
    }

    @Override
    public IBinder clone() {
        return new DistributionModel();
    }
}
