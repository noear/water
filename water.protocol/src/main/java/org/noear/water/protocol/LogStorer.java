package org.noear.water.protocol;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;

import java.util.Date;
import java.util.List;

public interface LogStorer {
    void write(String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime, String class_name, String thread_name) throws Exception;

    void writeAll(List<LogEvent> list);
}
