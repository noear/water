package webapp.model.water_msg;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/// <summary>
/// 生成:2017/12/21 04:30:34
/// 
/// </summary>
@Getter
public class MessageModel implements IBinder
{
    public long msg_id;
    public String msg_key;
    public int topic_id;
    public String topic_name;
    public String content;
    public int state;
    public int dist_count;
    public int dist_ntime;
    public int log_date;
    public Date log_fulltime;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        msg_id = s.get("msg_id").value(0L);
        msg_key = s.get("msg_key").value(null);
        topic_id = s.get("topic_id").value(0);
        topic_name = s.get("topic_name").value(null);
        content = s.get("content").value(null);
        state = s.get("state").value(0);
        dist_count = s.get("dist_count").value(0);
        dist_ntime = s.get("dist_ntime").value(0);
        log_date = s.get("log_date").value(0);
        log_fulltime = s.get("log_fulltime").value(null);
	}
	
	public IBinder clone()
	{
		return new MessageModel();
	}
}