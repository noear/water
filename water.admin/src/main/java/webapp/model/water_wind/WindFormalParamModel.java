package webapp.model.water_wind;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/// <summary>
/// 生成:2018/12/14 03:26:15
/// 
/// </summary>
@Getter
public class WindFormalParamModel implements IBinder
{
    public int id;
    public int param_id;
    public int script_id;
    public String param_name;
    public String param_note;

    public String param_value;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        id = s.get("id").value(0);
        param_id = s.get("param_id").value(0);
        script_id = s.get("script_id").value(0);
        param_name = s.get("param_name").value(null);
        param_note = s.get("param_note").value(null);
	}
	
	public IBinder clone()
	{
		return new WindFormalParamModel();
	}
}