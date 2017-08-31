package watersev.models.water;

import noear.weed.GetHandlerEx;
import noear.weed.IBinder;
import org.apache.http.util.TextUtils;

public class SynchronousModel implements IBinder {
    public int sync_id;
    public String key;
    public String name;
    public int type; //0,增量同步；1,更新同步；
    public int interval;
    public String target;
    public String target_pk;
    public String source;
    public String source_model;

    public long task_tag;

    @Override
    public void bind(GetHandlerEx s) {
        sync_id = s.get("sync_id").value(0);
        key = s.get("key").value("");
        name = s.get("name").value("");
        interval = s.get("interval").value(0);
        target = s.get("target").value(null);
        target_pk= s.get("target_pk").value(null);
        source = s.get("source").value("");
        source_model = s.get("source_model").value("");
        type = s.get("type").value(0);
        task_tag = s.get("task_tag").value(0l);
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
