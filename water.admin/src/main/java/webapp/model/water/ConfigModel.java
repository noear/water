package webapp.model.water;

import lombok.Getter;
import org.noear.weed.DbContext;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/// <summary>
/// 生成:2017/12/27 11:14:04
/// 
/// </summary>
@Getter
public class ConfigModel implements IBinder
{
    public int row_id;
    public String tag;
    public String key;
    public int type;
    public String url;
    public String user;
    public String password;
    public String explain;
    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        row_id = s.get("row_id").value(0);
        tag = s.get("tag").value(null);
        key = s.get("key").value(null);
        type = s.get("type").value(0);
        url = s.get("url").value(null);
        user = s.get("user").value(null);
        password = s.get("password").value(null);
        explain = s.get("explain").value(null);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new ConfigModel();
	}

	public String type_str(){
	    switch (type){
            case 10:return "数据库";
            case 11:return "Redis";
            case 12:return "MangoDb";
            case 20:return "Memcached";
            case 1001:return "阿里云账号";
            default:return "未知";
        }
    }

    public DbContext getDb() {
        DbContext db = new DbContext(key.replace("_r", "")
                                        .replace("_rtm",""), url, user, password);

        if(url.indexOf(":mysql:")>0){
            db.fieldFormatSet("`%`").objectFormatSet("`%`");
        }else if(url.indexOf(":postgresql:")>0){
            db.fieldFormatSet("\"%\"").objectFormatSet("\"%\"");
        }

        return db;
    }
}