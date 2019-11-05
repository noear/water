package webapp.model.water_wind;

import lombok.Data;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/// <summary>
/// 生成:2019/05/16 10:05:34
/// 
/// </summary>
@Data
public class WindDeployModel implements IBinder
{
    public int deploy_id;
    public int project_id;
    public String name;
    public String design_detail;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        deploy_id = s.get("deploy_id").value(0);
        project_id = s.get("project_id").value(0);
        name = s.get("name").value(null);
		design_detail = s.get("design_detail").value(null);
	}
	
	public IBinder clone()
	{
		return new WindDeployModel();
	}
}