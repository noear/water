package webapp.models.water_wind;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.*;
import java.util.*;

@Setter
@Getter
public class WindProjectResourceModel implements IBinder
{
    public int id;
    public int project_id;
    public String domain;
    public String port_plan;
    public int server_id;
    public String server_name;
    public int env_type;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        id = s.get("id").value(0);
        project_id = s.get("project_id").value(0);
        domain = s.get("domain").value(null);
        port_plan = s.get("port_plan").value(null);
        server_id = s.get("server_id").value(0);
        server_name = s.get("server_name").value(null);
        env_type = s.get("env_type").value(0);
	}
	
	public IBinder clone()
	{
		return new WindProjectResourceModel();
	}
}