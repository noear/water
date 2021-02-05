package org.noear.water.protocol.model.message;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/**
 * Created by noear on 2017/7/18.
 */
public class DistributionModel implements IBinder {
    public long dist_id;
    public long msg_id;
    public String msg_key;
    public int subscriber_id;
    public String subscriber_key;

    public String alarm_mobile;
    public String alarm_sign;

    public String receive_url;
    public int    receive_way;
    public String receive_key;

    //分发状态（-1忽略；0开始；1失败；2成功；）
    public int state;

    public Date _start_time;
    //派发处理的花费时间
    public long _duration = 0;
    public boolean _is_unstable;

    @Override
    public void bind(GetHandlerEx s) {
        dist_id = s.get("dist_id").value(0L);
        msg_id = s.get("msg_id").value(0L);
        msg_key = s.get("msg_key").value("");
        subscriber_id = s.get("subscriber_id").value(0);
        subscriber_key = s.get("subscriber_key").value("");

        alarm_mobile = s.get("alarm_mobile").value("");
        alarm_sign   = s.get("alarm_sign").value("");

        receive_url = s.get("receive_url").value("");
        receive_key = s.get("receive_key").value("");

        receive_way = s.get("receive_way").value(0);
        state = s.get("state").value(0);
    }

    @Override
    public IBinder clone() {
        return new DistributionModel();
    }
}
