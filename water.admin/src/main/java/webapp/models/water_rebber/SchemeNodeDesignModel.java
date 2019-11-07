package webapp.models.water_rebber;

import lombok.Getter;
import org.noear.weed.*;

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