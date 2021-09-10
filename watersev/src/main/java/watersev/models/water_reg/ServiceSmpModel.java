package watersev.models.water_reg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/**
 * Created by noear on 2017/7/28.
 */
public class ServiceSmpModel implements IBinder {
    public String name;
    public String address;
    public String meta;

    @Override
    public void bind(GetHandlerEx s) {
        name = s.get("name").value("");
        address = s.get("address").value("");
        meta = s.get("meta").value("");
    }

    @Override
    public IBinder clone() {
        return new ServiceSmpModel();
    }
}
