package wateradmin.models.water_paas;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2018/05/17 02:48:28
/// 
/// </summary>
@Getter
public class RebberActorModel implements IBinder
{
    public int actor_id;
    public String tag;
    public String name;
    public String name_display;
    public Date last_updatetime;
    public String note;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        actor_id = s.get("actor_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        last_updatetime = s.get("last_updatetime").value(null);
        note = s.get("note").value(null);
	}
	
	public IBinder clone()
	{
		return new RebberActorModel();
	}
}