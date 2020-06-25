package wateradmin.models.water_rebber;

import lombok.Getter;
import org.noear.weed.*;

/// <summary>
/// 生成:2018/05/17 01:22:08
/// 
/// </summary>
@Getter
public class SchemeNodeDesignModel implements IBinder
{
    public int scheme_id;
    public String details;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        scheme_id = s.get("scheme_id").value(0);
        details = s.get("details").value(null);
	}
	
	public IBinder clone()
	{
		return new SchemeNodeDesignModel();
	}
}