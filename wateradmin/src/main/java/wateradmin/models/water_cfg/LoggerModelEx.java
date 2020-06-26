package wateradmin.models.water_cfg;

import lombok.Getter;
import org.noear.water.protocol.model.LoggerModel;

@Getter
public class LoggerModelEx extends LoggerModel
{
    public int logger_id;
    public String tag;
    public String logger;
    public long row_num;
    public long row_num_today;
    public long row_num_today_error;
    public long row_num_yesterday;
    public long row_num_yesterday_error;
    public long row_num_beforeday;
    public long row_num_beforeday_error;

    public int keep_days;
    public String source;
    public String note;
    public long counts;
    public int is_enabled;

	public boolean isHighlight() {
        return (row_num_today_error > 0);
    }
}