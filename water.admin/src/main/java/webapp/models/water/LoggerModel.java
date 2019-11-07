package webapp.models.water;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2017/12/20 03:09:14
/// 
/// </summary>
@Getter
public class LoggerModel //implements IBinder
{
    public int id;
    public String tag;
    public String logger;
    public long row_num;
    public long row_num_today;
    public long row_num_yesterday;
    public long row_num_beforeday;
    public int keep_days;
    public String source;
    public String note;
    public long counts;
    public int is_enabled;

	public boolean isHighlight() {
        return (row_num_today > 0 && logger.indexOf("_error") > 0);
    }
}