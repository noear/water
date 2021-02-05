package wateradmin.models.water_msg;

import lombok.Getter;
import org.noear.water.utils.TextUtils;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2017/12/21 04:30:34
/// 
/// </summary>
//@Getter
//public class SubscriberModel implements IBinder
//{
//    public int subscriber_id;
//    public String subscriber_key;
//    public String subscriber_note;
//
//    public String alarm_mobile;
//    public String alarm_sign;
//
//    public int topic_id;
//    public String topic_name;
//
//    public String receive_url;
//    public int receive_way;
//    public String receive_key;
//
//    public int is_sync;
//    public Date log_fulltime;
//    public int is_enabled;
//
//    public int check_last_state;
//
//	public void bind(GetHandlerEx s)
//	{
//		//1.source:数据源
//		//
//        subscriber_id = s.get("subscriber_id").value(0);
//        subscriber_key = s.get("subscriber_key").value(null);
//        subscriber_note = s.get("subscriber_note").value(null);
//        alarm_mobile = s.get("alarm_mobile").value(null);
//        alarm_sign = s.get("alarm_sign").value(null);
//        topic_id = s.get("topic_id").value(0);
//        topic_name = s.get("topic_name").value(null);
//        receive_url = s.get("receive_url").value(null);
//        receive_way = s.get("receive_way").value(0);
//        receive_key = s.get("receive_key").value(null);
//        is_sync = s.get("is_sync").value(0);
//        log_fulltime = s.get("log_fulltime").value(null);
//        is_enabled = s.get("is_enabled").value(0);
//
//        check_last_state = s.get("check_last_state").value(0);
//	}
//
//	public IBinder clone()
//	{
//		return new SubscriberModel();
//	}
//
//	public String trClass() {
//        if (TextUtils.isEmpty(subscriber_note)) {
//            return "";
//        }else{
//            return (check_last_state == 200) ? "" : "t4";
//        }
//    }
//}