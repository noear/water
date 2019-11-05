package webapp.model.water_wind;

import lombok.Data;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/// <summary>
/// 生成:2019/05/17 10:38:33
/// 
/// </summary>
@Data
public class WindDeployNodeModel implements IBinder
{
    public long id;
    public int project_id;
    public int deploy_id;
    public int node_id;
    public int node_type;
    public int next_node_id;
    public int operate_id;
    public String note;
    public String node_key;
    public String prve_key;
    public String next_key;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        id = s.get("id").value(0L);
        project_id = s.get("project_id").value(0);
        deploy_id = s.get("deploy_id").value(0);
        node_id = s.get("node_id").value(0);
        node_type = s.get("node_type").value(0);
        next_node_id = s.get("next_node_id").value(0);
        operate_id = s.get("operate_id").value(0);
        note = s.get("note").value(null);
        node_key = s.get("node_key").value(null);
        prve_key = s.get("prve_key").value(null);
        next_key = s.get("next_key").value(null);
	}
	
	public IBinder clone()
	{
		return new WindDeployNodeModel();
	}
}