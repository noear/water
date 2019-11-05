package webapp.model.water_paas;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/// <summary>
/// 生成:2018/01/12 03:47:17
/// 
/// </summary>
@Getter
public class PaasApiModel implements IBinder
{
    public int api_id;
    public String tag;
    public String api_name;
    public String args;
    public String code;
    public String note;
    public int cache_time;
    public int is_post;
    public int is_get;
    public int is_enabled;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        api_id = s.get("api_id").value(0);
        tag = s.get("tag").value(null);
        api_name = s.get("api_name").value(null);
        args = s.get("args").value(null);
        code = s.get("code").value(null);
        note = s.get("note").value(null);
        cache_time = s.get("cache_time").value(0);
        is_post = s.get("is_post").value(1);
        is_get = s.get("is_get").value(1);
        is_enabled = s.get("is_enabled").value(0);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new PaasApiModel();
	}

	public String methods(){
	    if(is_get>0 && is_post>0)
	        return "G,P";

	    if(is_get>0)
	        return "G";

	    if(is_post>0)
	        return "P";

	    return "";
    }

    public String note_str(){
	    if(note == null || note.length()<10){
	        return note;
        }else{
	        return note.substring(0,10)+"...";
        }
    }

    public String note_hint() {
        if (note != null && note.length() > 10) {
            return note;
        } else {
            return "";
        }
    }
}