package wateradmin.models.water_log;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class LogSqlModel {
    public long log_id;
    public String trace_id;
    public String service;
    public String schema;
    public String method;
    public int seconds;
    public long interval;
    public String cmd_sql;
    public String cmd_arg;
    public String operator;
    public String operator_ip;
    public String path;
    public String ua;
    public int log_date;
    public Date log_fulltime;
}
