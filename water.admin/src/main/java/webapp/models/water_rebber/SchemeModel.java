package webapp.models.water_rebber;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2018/05/15 10:57:40
/// 
/// </summary>
@Getter
public class SchemeModel implements IBinder
{
    public int scheme_id;
    public String tag;
    public String name;
    public String name_display;
    public String related_model;
    public String event;
    public String related_block;
    public String debug_args;
    public int rule_relation;
    public int node_count;
    public int rule_count;
    public int is_enabled;
    public Date last_updatetime;
    public long counts;
    public int model_id;

    public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        scheme_id = s.get("scheme_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        related_model = s.get("related_model").value(null);
        event = s.get("event").value(null);
        related_block = s.get("related_block").value(null);
        debug_args = s.get("debug_args").value(null);
        rule_relation = s.get("rule_relation").value(0);
        node_count = s.get("node_count").value(0);
        rule_count = s.get("rule_count").value(0);
        is_enabled = s.get("is_enabled").value(0);
        last_updatetime = s.get("last_updatetime").value(null);
        counts = s.get("counts").value(0L);
    }
	
	public IBinder clone()
	{
		return new SchemeModel();
	}
}