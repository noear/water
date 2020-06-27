package org.noear.water.protocol;

import org.noear.water.log.Level;

public interface LogStorer {
    default void write(long log_id, String logger, Level level, Object content, String from) {
        write(log_id, logger, level, null, null, content, from);
    }

    default void write(long log_id, String logger, Level level, String tag, String summary, Object content, String from) {
        write(log_id, logger, level, tag, null, null, null, summary, content, from);
    }

    default void write(long log_id, String logger, Level level, String tag, String tag1, String summary, Object content, String from) {
        write(log_id, logger, level, tag, tag1, null, null, summary, content, from);
    }

    void write(long log_id, String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from);
}
