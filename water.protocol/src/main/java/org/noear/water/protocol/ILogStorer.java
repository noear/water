package org.noear.water.protocol;

import org.noear.water.log.Level;

/**
 * 日志存储器接口
 * */
public interface ILogStorer {
    default void append(String logger, Level level, Object content, String from) {
        append(logger, level, null, null, content, from);
    }

    default void append(String logger, Level level, String tag, String summary, Object content, String from) {
        append(logger, level, tag, null, null, null, summary, content, from);
    }

    default void append(String logger, Level level, String tag, String tag1, String summary, Object content, String from) {
        append(logger, level, tag, tag1, null, null, summary, content, from);
    }

    void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from);
}
