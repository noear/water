package watersev.models.water_paas;

import org.noear.solonjt.model.AFileModel;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

public class PaasFileModel extends AFileModel implements IBinder {

    public String alarm_sign;
    public String alarm_mobile;

    @Override
    public void bind(GetHandlerEx s) {
        file_id = s.get("file_id").value(0);
        tag = s.get("tag").value(null);
        label = s.get("label").value(null);
        path = s.get("path").value(null);
        is_staticize = s.get("is_staticize").value(false);
        is_editable = s.get("is_editable").value(false);
        is_disabled = s.get("is_disabled").value(false);
        link_to = s.get("link_to").value(null);
        edit_mode = s.get("edit_mode").value(null);
        content_type = s.get("content_type").value(null);
        content = s.get("content").value("");
        note = s.get("note").value(null);
        plan_state = s.get("plan_state").value(0);
        plan_begin_time = s.get("plan_begin_time").value(null);
        plan_last_time = s.get("plan_last_time").value(null);
        plan_last_timespan = s.get("plan_last_timespan").value(0l);
        plan_interval = s.get("plan_interval").value(null);
        plan_max = s.get("plan_max").value(0);
        plan_count = s.get("plan_count").value(0);
        create_fulltime = s.get("create_fulltime").value(null);
        update_fulltime = s.get("update_fulltime").value(null);
    }

    @Override
    public IBinder clone() {
        return new PaasFileModel();
    }
}
