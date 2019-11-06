package webapp.models.water_wind;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

@Getter
public class WindScriptModel implements IBinder
{
    public int script_id;
    public String tag;
    public String name;
    public int type;
    public String args;
    public String code;
    public int env;
    public String creator;
    public String modifier;
    public int is_enabled;
    public Date create_fulltime;
    public Date update_fulltime;

    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        script_id = s.get("script_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        type = s.get("type").value(0);
        args = s.get("args").value(null);
        code = s.get("code").value("");
        env = s.get("env").value(0);
        creator = s.get("creator").value(null);
        modifier = s.get("modifier").value(null);
        is_enabled = s.get("is_enabled").value(0);
        create_fulltime = s.get("create_fulltime").value(null);
        update_fulltime = s.get("update_fulltime").value(null);
        counts = s.get("counts").value(0L);
	}

	public String getEnum(){
	    String str = "";
	    if (env>0){
            String[] split = String.valueOf(env).split("");
            for (int i = 0; i < split.length; i++) {
                str += split[i];
                if(i<split.length-1){
                    str += ",";
                }
            }
        }
        return str;
    }
	
	public IBinder clone()
	{
		return new WindScriptModel();
	}
}