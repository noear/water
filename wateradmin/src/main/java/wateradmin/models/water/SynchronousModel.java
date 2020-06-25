package wateradmin.models.water;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2017/12/20 02:44:02
/// 
/// </summary>
@Getter
public class SynchronousModel implements IBinder
{
    public int sync_id;
    public String key;
    public String name;
    public int type;
    public int interval;
    public String target;
    public String target_pk;
    public String source_model;
    public long task_tag;
    public String alarm_mobile;
    public String alarm_sign;
    public int is_enabled;
    public Date last_fulltime;
    public String tag;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        sync_id = s.get("sync_id").value(0);
        key = s.get("key").value(null);
        name = s.get("name").value(null);
        type = s.get("type").value(0);
        interval = s.get("interval").value(0);
        target = s.get("target").value(null);
        target_pk = s.get("target_pk").value(null);
        source_model = s.get("source_model").value(null);
        task_tag = s.get("task_tag").value(0L);
        alarm_mobile = s.get("alarm_mobile").value(null);
        alarm_sign = s.get("alarm_sign").value(null);
        is_enabled = s.get("is_enabled").value(0);
        last_fulltime = s.get("last_fulltime").value(null);
        tag = s.get("tag").value(null);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new SynchronousModel();
	}
}