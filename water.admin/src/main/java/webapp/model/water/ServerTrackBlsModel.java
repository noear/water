package webapp.model.water;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/// <summary>
/// 生成:2017/12/28 11:55:12
/// 
/// </summary>
@Getter
public class ServerTrackBlsModel implements IBinder
{
    public int server_id;
    public String tag;
    public String name;
    public String iaas_key;
    public int iaas_type;

    public long co_conect_num;
    public long new_conect_num;
    public long qps;
    public long traffic_tx;

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

        co_conect_num = s.get("co_conect_num").value(0L);
        new_conect_num = s.get("new_conect_num").value(0L);
        qps = s.get("qps").value(0L);
        traffic_tx = s.get("traffic_tx").value(0L);

        last_updatetime = s.get("last_updatetime").value(null);
	}
	
	public IBinder clone()
	{
		return new ServerTrackBlsModel();
	}

    public String iaas_type_str(){
        switch (iaas_type){
            case 1:return "LBS";
            case 2:return "RDS";
            case 3:return "Redis";
            case 4:return "Memcached";
            case 5:return "DRDS";
            default:return "ECS";
        }
    }

}