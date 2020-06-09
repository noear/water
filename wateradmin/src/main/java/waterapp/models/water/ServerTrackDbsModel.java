package waterapp.models.water;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import waterapp.dso.IaasTypeUtils;

import java.util.Date;

/// <summary>
/// 生成:2017/12/28 11:55:12
/// 
/// </summary>
@Getter
public class ServerTrackDbsModel implements IBinder
{
    public int server_id;
    public String tag;
    public String name;
    public String iaas_key;
    public int iaas_type;
    public String iaas_attrs;

    public double connect_usage;
    public double cpu_usage;
    public double memory_usage;
    public double disk_usage;

    public Date last_updatetime;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        server_id = s.get("server_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        iaas_key = s.get("iaas_key").value(null);
        iaas_type = s.get("iaas_type").value(0);
        iaas_attrs = s.get("iaas_attrs").value(null);

        connect_usage = s.get("connect_usage").value(0d);
        cpu_usage = s.get("cpu_usage").value(0d);
        memory_usage = s.get("memory_usage").value(0d);
        disk_usage = s.get("disk_usage").value(0d);

        last_updatetime = s.get("last_updatetime").value(null);
	}
	
	public IBinder clone()
	{
		return new ServerTrackDbsModel();
	}

    public String iaas_type_str(){
        return IaasTypeUtils.iaas_type_str(iaas_type);
    }

}