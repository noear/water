package org.noear.water.protocol.model.message;

import lombok.Getter;
import org.noear.water.utils.Timespan;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/**
 * Created by noear on 2017/7/18.
 */
@Getter
public class MessageModel implements IBinder {
    public Long msg_id;
    public String msg_key;
    public String trace_id;

    public String tags;

    public String topic_name;

    public String content;

    public boolean dist_routed;
    public int dist_count;
    public long dist_nexttime;

    //消息状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
    public int state;

    //public String lk_msg_id_do;

    public int log_date;
    public Date log_fulltime;
    public Date last_fulltime;

    @Override
    public void bind(GetHandlerEx s) {
        msg_id = s.get("msg_id").value(0L);
        msg_key = s.get("msg_key").value("");

        trace_id = s.get("trace_id").value("");

        topic_name = s.get("topic_name").value("");

        content = s.get("content").value("");
        tags = s.get("tags").value("");

        state = s.get("state").value(0);

        dist_routed = s.get("dist_routed").value(false);
        dist_count = s.get("dist_count").value(0);
        dist_nexttime = s.get("dist_nexttime").longValue(0L);

        log_date = s.get("log_date").value(0);
        log_fulltime = s.get("log_fulltime").value(null);
        last_fulltime = s.get("last_fulltime").value(null);

        if (last_fulltime == null) {
            last_fulltime = new Date();
        }
    }

    @Override
    public IBinder clone() {
        return new MessageModel();
    }


    /**
     * 状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
     */
    public String stateStr() {
        switch (state) {
            case -2:
                return "-2 无派发对象";
            case -1:
                return "-1 忽略";
            case 1:
                return "1 处理中";
            case 2:
                return "2 已成功";
            case 3:
                return "3 派发超数";
            default:
                return "0 未处理";
        }
    }

    public String nexttime(long c) {
        if (dist_nexttime == 0) {
            return "" + nexttimeDo(log_fulltime.getTime(), last_fulltime.getTime());
        }

        if (state > 1 || state < 0) {
            return "" + nexttimeDo(dist_nexttime, last_fulltime.getTime());
        } else {
            return "" + nexttimeDo(dist_nexttime, c);
        }
    }

    private String nexttimeDo(long time1, long time2) {
        Timespan ts = new Timespan(time1, time2);

        if (ts.minutes() < 1) {
            return ts.seconds() + "s";
        }

        if (ts.hours() < 1) {
            return ts.minutes() + "m";
        }

        long hours = ts.hours();

        if (hours < 99) {
            return hours + "h";
        }

        return (hours / 24) + "d";
    }
}
