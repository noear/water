package webapp.model.water_log;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

@Getter
public class LogSqlModel implements IBinder
{
    public long log_id;
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
    public String note;
    public int log_date;
    public int log_hour;
    public Date log_fulltime;

    public String tag;

    public void bind(GetHandlerEx s)
    {
        //1.source:数据源
        //
        log_id = s.get("log_id").value(0L);
        service = s.get("service").value(null);
        schema = s.get("schema").value(null);
        method = s.get("method").value(null);
        seconds = s.get("seconds").value(0);
        interval = s.get("interval").value(0L);
        cmd_sql = s.get("cmd_sql").value(null);
        cmd_arg = s.get("cmd_arg").value(null);
        operator = s.get("operator").value(null);
        operator_ip = s.get("operator_ip").value(null);
        path = s.get("path").value(null);
        ua = s.get("ua").value(null);
        note = s.get("note").value(null);
        log_date = s.get("log_date").value(0);
        log_hour = s.get("log_hour").value(0);
        log_fulltime = s.get("log_fulltime").value(null);

        tag = s.get("tag").stringValue("");
    }

    public IBinder clone()
    {
        return new LogSqlModel();
    }
}
