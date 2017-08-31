package watersev.models.water;

import noear.weed.GetHandlerEx;
import noear.weed.IBinder;

public class MonitorModel implements IBinder {
    public int monitor_id;
    public int type; //监视类型（0:数据表预警；1:数据表报喜）
    public String name;
    public String source;
    public String source_model;
    public String task_tag;
    public String task_tag_exp;
    public String rule;
    public String alarm_mobile;
    public String alarm_exp;
    public int    alarm_count;

    @Override
    public void bind(GetHandlerEx s) {
        monitor_id = s.get("monitor_id").value(0);
        type = s.get("type").value(0);
        name = s.get("name").value("");
        source = s.get("source").value("");
        source_model = s.get("source_model").value("");
        rule = s.get("rule").value("");

        task_tag = s.get("task_tag").value("");
        task_tag_exp = s.get("task_tag_exp").value("");

        alarm_mobile = s.get("alarm_mobile").value("");
        alarm_exp = s.get("alarm_exp").value("");
        alarm_count = s.get("alarm_count").value(0);
    }

    @Override
    public IBinder clone() {
        return new MonitorModel();
    }
}
