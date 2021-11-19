package watersev.models.water_msg;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

public class TopicModel implements IBinder {
    public int topic_id;
    public int max_msg_num;
    public int max_distribution_num;//最大派发次数（0不限）
    public int max_concurrency_num;//最大同时派发数(0不限）

    public int alarm_model; //报警模式：0=普通模式；1=不报警

    @Override
    public void bind(GetHandlerEx s) {
        topic_id = s.get("topic_id").value(0);
        max_msg_num = s.get("max_msg_num").value(0);
        max_distribution_num = s.get("max_distribution_num").value(0);
        max_concurrency_num = s.get("max_concurrency_num").value(0);
        alarm_model = s.get("alarm_model").value(0);
    }

    @Override
    public IBinder clone() {
        return new TopicModel();
    }

}
