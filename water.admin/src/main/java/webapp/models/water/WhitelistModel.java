package webapp.models.water;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2017/12/22 01:23:34
/// 
/// </summary>
@Getter
public class WhitelistModel implements IBinder
{
    public int id;
    public String tag;
    public String ip;
    public String note;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        id = s.get("id").value(0);
        tag = s.get("tag").value(null);
        ip = s.get("ip").value(null);
        note = s.get("note").value(null);
	}
	
	public IBinder clone()
	{
		return new WhitelistModel();
	}
}