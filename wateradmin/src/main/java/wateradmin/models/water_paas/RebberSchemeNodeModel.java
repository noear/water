package wateradmin.models.water_paas;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2018/05/15 10:57:40
/// 
/// </summary>
@Getter
public class RebberSchemeNodeModel implements IBinder
{
    public int node_id;
    public String node_key;
    public int scheme_id;
    public int type;
    public String name;
    public String prve_key;
    public String next_key;
    public String condition;
    public String tasks;
    public String actor;
    public String actor_display;
    public Date last_updatetime;
    public int is_enabled;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        node_id = s.get("node_id").value(0);
        node_key = s.get("node_key").value(null);
        scheme_id = s.get("scheme_id").value(0);
        type = s.get("type").value(0);
        name = s.get("name").value(null);
        prve_key = s.get("prve_key").value(null);
        next_key = s.get("next_key").value(null);
        condition = s.get("condition").value(null);
        tasks = s.get("tasks").value(null);
        actor = s.get("actor").value(null);
        actor_display = s.get("actor_display").value(null);
        last_updatetime = s.get("last_updatetime").value(null);
        is_enabled = s.get("is_enabled").value(0);
	}
	
	public IBinder clone()
	{
		return new RebberSchemeNodeModel();
	}
}