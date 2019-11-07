package webapp.models.water_paas;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

@Getter
public class PaasPlanModel implements IBinder
{
    public int plan_id;
    public int plan_type;
    public String tag;
    public String plan_name;
    public String source;
    public String code;
    public Date begin_time;
    public Date last_exec_time;
    public String last_exec_note;
    public String repeat_interval;
    public int repeat_max;
    public int repeat_count;
    public int is_enabled;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        plan_id = s.get("plan_id").value(0);
        plan_type = s.get("plan_type").value(0);
        tag = s.get("tag").value(null);
        plan_name = s.get("plan_name").value(null);
        source = s.get("source").value(null);
        code = s.get("code").value(null);
        begin_time = s.get("begin_time").value(null);
        last_exec_time = s.get("last_exec_time").value(null);
        last_exec_note = s.get("last_exec_note").value(null);
        repeat_interval = s.get("repeat_interval").value(null);
        repeat_max = s.get("repeat_max").value(0);
        repeat_count = s.get("repeat_count").value(0);
        is_enabled = s.get("is_enabled").value(0);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new PaasPlanModel();
	}
}