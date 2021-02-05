package wateradmin.models.water_msg;

import lombok.Getter;
import org.noear.water.utils.Timespan;
import org.noear.weed.*;
import java.util.*;

//@Getter
//public class MessageModel implements IBinder {
//    public long msg_id;
//    public String msg_key;
//    public String trace_id;
//
//    public String tags;
//
//    public int topic_id;
//    public String topic_name;
//
//    public String content;
//
//    public int state;
//    public int dist_count;
//    public long dist_nexttime;
//
//    public int log_date;
//    public Date log_fulltime;
//    public Date last_fulltime;
//
//    public void bind(GetHandlerEx s) {
//        //1.source:数据源
//        //
//        msg_id = s.get("msg_id").value(0L);
//        msg_key = s.get("msg_key").value(null);
//        trace_id = s.get("trace_id").value(null);
//        tags = s.get("tags").value(null);
//        topic_id = s.get("topic_id").value(0);
//        topic_name = s.get("topic_name").value(null);
//        content = s.get("content").value(null);
//        state = s.get("state").value(0);
//        dist_count = s.get("dist_count").value(0);
//        dist_nexttime = s.get("dist_nexttime").value(0l);
//        log_date = s.get("log_date").value(0);
//        log_fulltime = s.get("log_fulltime").value(null);
//        last_fulltime = s.get("last_fulltime").value(null);
//
//        if(last_fulltime == null){
//            last_fulltime = log_fulltime;
//        }
//    }
//
//    /**
//     * 状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
//     * */
//    public String stateStr(){
//        switch (state){
//            case -2:return "-2 无派发对象";
//            case -1:return "-1 忽略";
//            case 1:return "1 处理中";
//            case 2:return "2 已成功";
//            case 3:return "3 派发超数";
//            default:return "0 未处理";
//        }
//    }
//
//    public IBinder clone() {
//        return new MessageModel();
//    }
//
//    public String nexttime(long c) {
//        if(dist_nexttime == 0){
//            return "*";
//        }
//
//        if (dist_nexttime > c) {
//            return nexttimeDo(dist_nexttime, c);
//        } else {
//            return "-" + nexttimeDo(c, dist_nexttime);
//        }
//    }
//
//    private String nexttimeDo(long time1, long time2) {
//        Timespan ts = new Timespan(time1, time2);
//
//        if (ts.minutes() < 1) {
//            return ts.seconds() + "s";
//        }
//
//        if (ts.hours() < 1) {
//            return ts.minutes() + "m";
//        }
//
//        long hours = ts.hours();
//
//        if (hours < 99) {
//            return hours + "h";
//        }
//
//        return (hours / 24) + "d";
//    }
//}