package webapp.model.water;

import lombok.Getter;

/// <summary>
/// 生成:2017/12/20 03:09:14
/// 
/// </summary>
@Getter
public class LoggerModel //implements IBinder
{
    public int logger_id;
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


//    public void bind(GetHandlerEx s)
//	{
//		//1.source:数据源
//		//
//        logger_id = s.get("logger_id").value(0);
//        tag = s.get("tag").value(null);
//        logger = s.get("logger").value(null);
//        row_num = s.get("row_num").value(0L);
//        row_num_today = s.get("row_num_today").value(0L);
//        row_num_yesterday = s.get("row_num_yesterday").value(0L);
//        row_num_beforeday = s.get("row_num_beforeday").value(0L);
//        keep_days = s.get("keep_days").value(0);
//        source = s.get("source").value(null);
//        note = s.get("note").value(null);
//        counts = s.get("counts").value(0L);
//        is_enabled = s.get("is_enabled").value(0);
//	}
//
//	public IBinder clone()
//	{
//		return new LoggerModel();
//	}

	public boolean isHighlight() {
        return (row_num_today > 0 && logger.indexOf("_error") > 0);
    }
}