package org.noear.water.protocol;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;

import java.util.Date;
import java.util.List;

public interface LogStorer {
    default void write(String logger, Level level, Object content, String from) {
        write(logger, level, null, null, content, from);
    }

    default void write(String logger, Level level, String tag, String summary, Object content, String from) {
        write(logger, level, tag, null, null, null, summary, content, from);
    }

    default void write(String logger, Level level, String tag, String tag1, String summary, Object content, String from) {
        write(logger, level, tag, tag1, null, null, summary, content, from);
    }

    default void write(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from) {
        write(logger, level, tag, tag1, tag2, tag3, summary, content, from, null);
    }

    void write(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime);

    void writeAll(List<LogEvent> list);
}
