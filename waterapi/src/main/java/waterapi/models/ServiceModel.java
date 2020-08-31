package waterapi.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

public class ServiceModel implements IBinder {
    public String name;
    public String address;
    public String note;
    public String check_last_note;

    @Override
    public void bind(GetHandlerEx s) {
        name = s.get("name").value("");
        address = s.get("address").value("");
        note = s.get("note").value("");
        check_last_note = s.get("check_last_note").value("");
    }

    @Override
    public IBinder clone() {
        return new ServiceModel();
    }
}
