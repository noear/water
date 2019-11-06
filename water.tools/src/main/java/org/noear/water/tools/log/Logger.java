package org.noear.water.tools.log;

public interface Logger {
    String getName();

    void trace(Object content);
    void trace(String summary, Object content);
    void trace(String tag, String summary, Object content);
    void trace(String tag, String tag1, String summary, Object content);
    void trace(String tag, String tag1, String tag2, String summary, Object content);
    void trace(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    void debug(Object content);
    void debug(String summary, Object content);
    void debug(String tag, String summary, Object content);
    void debug(String tag, String tag1, String summary, Object content);
    void debug(String tag, String tag1, String tag2, String summary, Object content);
    void debug(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    void info(Object content);
    void info(String summary, Object content);
    void info(String tag, String summary, Object content);
    void info(String tag, String tag1, String summary, Object content);
    void info(String tag, String tag1, String tag2, String summary, Object content);
    void info(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    void warn(Object content);
    void warn(String summary, Object content);
    void warn(String tag, String summary, Object content);
    void warn(String tag, String tag1, String summary, Object content);
    void warn(String tag, String tag1, String tag2, String summary, Object content);
    void warn(String tag, String tag1, String tag2, String tag3, String summary, Object content);

    void error(Throwable content);
    void error(String summary, Throwable content);
    void error(String tag, String summary, Throwable content);
    void error(String tag, String tag1, String summary, Throwable content);
    void error(String tag, String tag1, String tag2, String summary, Throwable content);
    void error(String tag, String tag1, String tag2, String tag3, String summary, Throwable content);
}
