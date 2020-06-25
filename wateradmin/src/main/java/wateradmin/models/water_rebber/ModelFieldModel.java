package wateradmin.models.water_rebber;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2018/05/15 10:57:40
/// 
/// </summary>
@Getter
public class ModelFieldModel implements IBinder
{
    public transient int field_id;

    public int model_id;
    public String name;
    public String name_display;
    public String note;
    public String expr;
    public Date last_updatetime;
    public int is_pk;

    public transient String tag;//需要与model关联查询

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        field_id = s.get("field_id").value(0);
        model_id = s.get("model_id").value(0);
        name = s.get("name").value(null);
        name_display = s.get("name_display").value(null);
        note = s.get("note").value(null);
        expr = s.get("expr").value(null);
        last_updatetime = s.get("last_updatetime").value(null);
        is_pk = s.get("is_pk").value(0);

        tag = s.get("tag").value(null);
	}
	
	public IBinder clone()
	{
		return new ModelFieldModel();
	}

}