package webapp.model.water_wind;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/// <summary>
/// 生成:2018/12/11 01:58:54
/// 
/// </summary>
@Getter
public class WindOperateModel implements IBinder
{
    public int operate_id;
    public int server_id;
    public int service_id;
    public String name;
    public int type;
    public int rank;
    public String note;
    public int script_id;
    public String args;
    public String creator;
    public Date create_fulltime;
    public String modifier;
    public Date update_fulltime;

    public String script_name;
    public String script_tag;
    public String targets;

    public List<String> getTargets() {
        String[] ts = targets.split(",");
        List<String> list = new ArrayList<>();
        for (String t: ts) {
            list.add(t);
        }
        return list;
    }

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        operate_id = s.get("operate_id").value(0);
        server_id = s.get("server_id").value(0);
        service_id = s.get("service_id").value(0);
        name = s.get("name").value(null);
        type = s.get("type").value(0);
        rank = s.get("rank").value(0);
        note = s.get("note").value(null);
        script_id = s.get("script_id").value(0);
        args = s.get("args").value(null);
        creator = s.get("creator").value(null);
        create_fulltime = s.get("create_fulltime").value(null);
        modifier = s.get("modifier").value(null);
        update_fulltime = s.get("update_fulltime").value(null);

        script_name = s.get("script_name").value(null);
        script_tag = s.get("script_tag").value(null);
        targets = s.get("targets").value("");
	}

	public IBinder clone()
	{
		return new WindOperateModel();
	}
}