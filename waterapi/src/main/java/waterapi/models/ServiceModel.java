package waterapi.models;

import org.noear.wood.GetHandlerEx;
import org.noear.wood.IBinder;

import java.util.Date;

public class ServiceModel implements IBinder {
    public String name;
    public String address;
    public String meta;
    public int state;
    public String code_location;
    public int check_type;
    public String check_url;
    public Date check_last_time;
    public int check_last_state;
    public String check_last_note;

    @Override
    public void bind(GetHandlerEx s) {
        name = s.get("name").value("");
        address = s.get("address").value("");
        meta = s.get("meta").value("");
        state = s.get("state").intValue(0);
        code_location = s.get("code_location").value("");
        check_type = s.get("state").intValue(0);
        check_url = s.get("check_url").value("");
        check_last_time = s.get("check_last_time").dateValue(null);
        check_last_state = s.get("check_last_state").intValue(0);
        check_last_note = s.get("check_last_note").value("");

        if (check_last_time == null) {
            check_last_time = new Date();
        }
    }

    @Override
    public IBinder clone() {
        return new ServiceModel();
    }
}
