package wateradmin.models.water_msg;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2017/12/21 04:30:34
/// 
/// </summary>
@Getter
public class DistributionModel implements IBinder
{
    public long dist_id;
    public long msg_id;
    public int subscriber_id;
    public String alarm_mobile;
    public String alarm_sign;
    public String receive_url;
    public int receive_way;
    public String access_key;
    public int is_sync;
    public int duration;
    public int state;
    public int log_date;
    public Date log_fulltime;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        dist_id = s.get("dist_id").value(0L);
        msg_id = s.get("msg_id").value(0L);
        subscriber_id = s.get("subscriber_id").value(0);
        alarm_mobile = s.get("alarm_mobile").value(null);
        alarm_sign = s.get("alarm_sign").value(null);
        receive_url = s.get("receive_url").value(null);
        receive_way = s.get("receive_way").value(0);
        access_key = s.get("access_key").value(null);
        is_sync = s.get("is_sync").value(0);
        duration = s.get("duration").value(0);
        state = s.get("state").value(0);
        log_date = s.get("log_date").value(0);
        log_fulltime = s.get("log_fulltime").value(null);
	}
	
	public IBinder clone()
	{
		return new DistributionModel();
	}
}