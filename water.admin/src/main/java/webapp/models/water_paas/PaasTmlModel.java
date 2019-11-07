package webapp.models.water_paas;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
@Setter
public class PaasTmlModel implements IBinder {

    public int tml_id;
    public String tag;
    public String tml_name;
    public String code;
    public String name_display;
    public int is_enabled;

    @Override
    public void bind(GetHandlerEx source) {
        tml_id = source.get("tml_id").value(0);
        tag = source.get("tag").value("");
        tml_name = source.get("tml_name").value("");
        name_display = source.get("name_display").value("");
        code = source.get("code").value(null);
        is_enabled = source.get("is_enabled").value(0);
    }

    @Override
    public IBinder clone() {
        return new PaasTmlModel();
    }
}
