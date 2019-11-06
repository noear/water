package webapp.models.water_paas;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/// <summary>
/// 生成:2018/01/12 06:15:09
/// 
/// </summary>
@Getter
public class PaasFunModel implements IBinder
{
    public int fun_id;
    public String tag;
    public String fun_name;
    public String name_display;
    public String args;
    public String code;
    public String note;
    public int is_enabled;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        fun_id = s.get("fun_id").value(0);
        tag = s.get("tag").value(null);
        fun_name = s.get("fun_name").value(null);
        name_display = s.get("name_display").value(null);
        args = s.get("args").value(null);
        code = s.get("code").value(null);
        note = s.get("note").value(null);
        is_enabled = s.get("is_enabled").value(0);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new PaasFunModel();
	}
}