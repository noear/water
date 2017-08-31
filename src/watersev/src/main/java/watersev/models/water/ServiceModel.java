package watersev.models.water;

import noear.weed.GetHandlerEx;
import noear.weed.IBinder;

import java.util.Date;

/**
 * Created by yuety on 2017/7/28.
 */
public class ServiceModel implements IBinder {
    public int service_id;
    public String key;
    public String name;
    public String address;
    public String note;
    public String alarm_mobile;

    public String check_url;
    public int check_type; // 0被动检查；2主动签到
    public int check_last_state; // 0:ok, 1:error
    public String check_last_note;

    //public Date check_last_time;
    //public String check_last_note;

    @Override
    public void bind(GetHandlerEx s) {
        service_id = s.get("service_id").value(0);
        key = s.get("key").value("");
        name = s.get("name").value(null);
        address = s.get("address").value("");
        note = s.get("note").value("");
        alarm_mobile = s.get("alarm_mobile").value("");

        check_url = s.get("check_url").value(null);
        check_type = s.get("check_type").value(0);
        check_last_state = s.get("check_last_state").value(0);
        check_last_note = s.get("check_last_note").value("");
    }

    @Override
    public IBinder clone() {
        return new ServiceModel();
    }
}
