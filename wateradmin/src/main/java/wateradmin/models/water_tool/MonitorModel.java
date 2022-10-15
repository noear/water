package wateradmin.models.water_tool;

import lombok.Getter;
import org.noear.wood.*;

/// <summary>
/// 生成:2017/12/20 02:44:02
/// 
/// </summary>
@Getter
public class MonitorModel implements IBinder
{
    public int monitor_id;
    public String key;
    public String name;
    public int type;
    public String source_query;
    public String rule;
    public String task_tag;
    public String task_tag_exp;
    public String alarm_mobile;
    public String alarm_exp;
    public int alarm_count;
    public String alarm_sign;
    public int is_enabled;
    public String tag;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
        monitor_id = s.get("monitor_id").value(0);
        tag = s.get("tag").value(null);
        key = s.get("key").value(null);
        name = s.get("name").value(null);
        type = s.get("type").value(0);
        source_query = s.get("source_query").value(null);
        rule = s.get("rule").value(null);
        task_tag = s.get("task_tag").value(null);
        task_tag_exp = s.get("task_tag_exp").value(null);
        alarm_mobile = s.get("alarm_mobile").value(null);
        alarm_exp = s.get("alarm_exp").value(null);
        alarm_count = s.get("alarm_count").value(0);
        alarm_sign = s.get("alarm_sign").value(null);
        is_enabled = s.get("is_enabled").value(0);
	}
	
	public IBinder clone()
	{
		return new MonitorModel();
	}

	public String type_str(){
	    if(type==1){
	        return "数据简报";
        }else{
	        return "数据预警";
        }
    }
}