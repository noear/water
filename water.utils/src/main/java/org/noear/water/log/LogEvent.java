package org.noear.water.log;

import java.util.Date;

public class LogEvent {
    public long log_id;
    public String logger;
    public String trace_id;
    public int level;
    public String tag;
    public String tag1;
    public String tag2;
    public String tag3;
    public String summary;
    public Object content;
    public String class_name;
    public String thread_name;
    public String from;
    public int log_date;
    public Date log_fulltime;
}
