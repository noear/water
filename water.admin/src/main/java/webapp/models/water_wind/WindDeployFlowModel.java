package webapp.models.water_wind;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class WindDeployFlowModel implements IBinder
{
    public long flow_id;
    public long task_id;
    public int deploy_id;
    public int project_id;
    public String version;
    public String project_name;
    public String developer;
    public String product_manager;
    public String domain;
    public String note;
    public int is_over;
    public Date create_fulltime;
    public int node_id;
    public String input;
    public int status;

    public String desc;

    public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        flow_id = s.get("flow_id").value(0L);
        task_id = s.get("task_id").value(0L);
        deploy_id = s.get("deploy_id").value(0);
        project_id = s.get("project_id").value(0);
        version = s.get("version").value(null);
        project_name = s.get("project_name").value(null);
        developer = s.get("developer").value(null);
        product_manager = s.get("product_manager").value(null);
        domain = s.get("domain").value(null);
        note = s.get("note").value(null);
        is_over = s.get("is_over").value(0);
        create_fulltime = s.get("create_fulltime").value(null);
        node_id = s.get("node_id").value(0);
        input = s.get("input").value("");
        status = s.get("status").value(0);
        desc  =s.get("desc").value("");

	}
	
	public IBinder clone()
	{
		return new WindDeployFlowModel();
	}
}