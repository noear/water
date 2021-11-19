package wateradmin.models.water_paas;

import lombok.Getter;
import org.noear.weed.*;

import java.util.Date;

/// <summary>
/// 生成:2018/05/17 01:22:08
/// 
/// </summary>
@Getter
public class RebberSchemeNodeDesignModel implements IBinder
{
    public int scheme_id;
    public String details;
	public Date last_updatetime;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        scheme_id = s.get("scheme_id").value(0);
        details = s.get("details").value(null);
		last_updatetime = s.get("last_updatetime").dateValue(null);

		if (last_updatetime == null) {
			last_updatetime = new Date();
		}
	}
	
	public IBinder clone()
	{
		return new RebberSchemeNodeDesignModel();
	}
}