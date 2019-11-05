package webapp.model.water_wind;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/// <summary>
/// 生成:2017/12/28 11:55:12
///
/// </summary>
@Getter
public class WindDeployTaskModel implements IBinder
{
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
    public String desc = "";

    public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
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
	}
	
	public IBinder clone()
	{
		return new WindDeployTaskModel();
	}
}