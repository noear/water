package webapp.models.water_rebber;

import lombok.Getter;
import org.noear.weed.*;

import java.util.*;

@Getter
public class ModelModel implements IBinder
{
    public int model_id;
    public String tag;
    public String name;
    public String name_display;
    public String related_db;
    public String init_expr;
    public String debug_args;
    public int field_count;
    public Date last_updatetime;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        model_id = s.get("model_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        related_db = s.get("related_db").value(null);
        init_expr = s.get("init_expr").value(null);
        debug_args = s.get("debug_args").value(null);
        field_count = s.get("field_count").value(0);
        last_updatetime = s.get("last_updatetime").value(null);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new ModelModel();
	}
}