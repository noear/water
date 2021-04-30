package watersev.models.water;

import org.noear.water.utils.TextUtils;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

public class SynchronousModel implements IBinder {
    public int sync_id;
    public String key;
    public String tag;
    public String name;
    public int type; //0,增量同步；1,更新同步；
    public int interval;
    public String target;
    public String target_pk;
    public String source_model;

    public String alarm_mobile;
    public String alarm_sign;

    public long task_tag;

    public Date last_fulltime;

    @Override
    public void bind(GetHandlerEx s) {
        sync_id = s.get("sync_id").value(0);
        key = s.get("key").value("");
        tag = s.get("tag").value("");
        name = s.get("name").value("");
        interval = s.get("interval").value(0);
        target = s.get("target").value(null);
        target_pk= s.get("target_pk").value(null);
        source_model = s.get("source_model").value("");
        type = s.get("type").value(0);
        task_tag = s.get("task_tag").value(0l);

        alarm_mobile = s.get("alarm_mobile").value("");
        alarm_sign   = s.get("alarm_sign").value("");

        last_fulltime = s.get("last_fulltime").value(null);

    }

    @Override
    public IBinder clone() {
        return new SynchronousModel();
    }

    public String getTitle() {
        if (TextUtils.isEmpty(name)) {
            return target;
        } else {
            return name + "#" + target;
        }
    }
}
