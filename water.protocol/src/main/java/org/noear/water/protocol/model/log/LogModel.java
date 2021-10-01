package org.noear.water.protocol.model.log;


import lombok.Getter;
import org.noear.water.log.Level;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.HtmlEncode;
import org.noear.water.utils.TextUtils;

import java.util.Date;

@Getter
public class LogModel {
    public long log_id;
    public String trace_id;
    public int level;
    public String tag;
    public String tag1;
    public String tag2;
    public String tag3;
    public String summary;
    public String class_name;
    public String thread_name;
    public String content;
    public String from;
    public int log_date;
    public Date log_fulltime;
}
