package watersev.models.water_reg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/**
 * Created by noear on 2017/7/28.
 */
public class ServiceModel implements IBinder {
    public int service_id;
    public String key;
    public String tag;
    public String name;
    public String address;
    public String note;
    public String alarm_mobile;
    public String alarm_sign;

    public int state;

    public String check_url;
    public int check_type; // 0被动检查；2主动签到
    public int check_error_num; // 0:ok, 1:error
    public String check_last_note;
    public Date check_last_time;

    public boolean is_enabled;
    public boolean is_unstable;

    @Override
    public void bind(GetHandlerEx s) {
        service_id = s.get("service_id").value(0);
        key = s.get("key").value("");
        tag = s.get("tag").value("");
        name = s.get("name").value(null);
        address = s.get("address").value("");
        note = s.get("note").value("");
        alarm_mobile = s.get("alarm_mobile").value("");
        alarm_sign   = s.get("alarm_sign").value("");

        state = s.get("state").intValue(0);

        check_url = s.get("check_url").value(null);
        check_type = s.get("check_type").value(0);
        check_error_num = s.get("check_error_num").value(0);
        check_last_time = s.get("check_last_time").value(new Date());
        check_last_note = s.get("check_last_note").value("");

        is_enabled = s.get("is_enabled").intValue(0) > 0;
        is_unstable = s.get("is_unstable").intValue(0) > 0;
    }

    @Override
    public IBinder clone() {
        return new ServiceModel();
    }
}
