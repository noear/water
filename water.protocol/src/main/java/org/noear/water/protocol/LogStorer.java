package org.noear.water.protocol;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;

import java.util.Date;
import java.util.List;

public interface LogStorer {
    default void write(String logger, String trace_id, Level level, Object content, String from) {
        write(logger, trace_id, level, null, null, content, from);
    }

    default void write(String logger, String trace_id, Level level, String tag, String summary, Object content, String from) {
        write(logger, trace_id, level, tag, null, null, null, summary, content, from);
    }

    default void write(String logger, String trace_id, Level level, String tag, String tag1, String summary, Object content, String from) {
        write(logger, trace_id, level, tag, tag1, null, null, summary, content, from);
    }

    default void write(String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from) {
        write(logger, trace_id, level, tag, tag1, tag2, tag3, summary, content, from, null);
    }

    void write(String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime);

    void writeAll(List<LogEvent> list);
}
