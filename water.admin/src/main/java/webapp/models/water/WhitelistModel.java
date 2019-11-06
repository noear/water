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
    public int row_id;
    public String tag;
    public String ip;
    public String note;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        row_id = s.get("row_id").value(0);
        tag = s.get("tag").value(null);
        ip = s.get("ip").value(null);
        note = s.get("note").value(null);
	}
	
	public IBinder clone()
	{
		return new WhitelistModel();
	}
}