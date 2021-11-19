package watersev.models.water_reg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/**
 * Created by noear on 2017/7/28.
 */
public class ServiceSmpModel implements IBinder {
    public String name;
    public String address;
    public String meta;
    public Date check_last_time;

    @Override
    public void bind(GetHandlerEx s) {
        name = s.get("name").value("");
        address = s.get("address").value("");
        meta = s.get("meta").value("");
        check_last_time = s.get("check_last_time").dateValue(null);

        if (check_last_time == null) {
            check_last_time = new Date();
        }
    }

    @Override
    public IBinder clone() {
        return new ServiceSmpModel();
    }
}
