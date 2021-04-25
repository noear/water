package waterapi.models;

/**
 * @author noear 2021/4/25 created
 */
public class TrackEvent {
    public long log_id;
    public String service;
    public String trace_id;
    public String schema;
    public String method;
    public String cmd_sql;
    public String cmd_sql_md5;
    public String cmd_arg;
    public int seconds;
    public long interval;
    public String operator;
    public String operator_ip;
    public String path;
    public String ua;
    public String note;
    public int log_date;
    public int log_hour;
    public long log_fulltime;
}
