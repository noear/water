package waterapp.models.water_msg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/**
 * Created by noear on 2017/7/18.
 */
public class DistributionModel implements IBinder {
    public long dist_id;
    public long msg_id;
    public int subscriber_id;

    public String alarm_mobile;
    public String alarm_sign;

    public String receive_url;
    public int    receive_way;
    public String access_key;

    //分发状态（-1忽略；0开始；1失败；2成功；）
    public int state;

    public Date _start_time;
    public long _duration = 0;
    public boolean _is_unstable;

    @Override
    public void bind(GetHandlerEx s) {
        dist_id = s.get("dist_id").value(0l);
        msg_id = s.get("msg_id").value(0l);
        subscriber_id = s.get("subscriber_id").value(0);

        alarm_mobile = s.get("alarm_mobile").value("");
        alarm_sign   = s.get("alarm_sign").value("");

        receive_url = s.get("receive_url").value("");
        access_key = s.get("access_key").value("");

        receive_way = s.get("receive_way").value(0);
        state = s.get("state").value(0);
    }

    @Override
    public IBinder clone() {
        return new DistributionModel();
    }
}
