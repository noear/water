package org.noear.water.client.dso.log;

public interface Logger {
    String getName();

    void trace(String content);
    void trace(String summary,String content);
    void trace(String tag,String summary,String content);
    void trace(String tag,String tag1,String summary,String content);
    void trace(String tag,String tag1,String tag2,String summary,String content);
    void trace(String tag,String tag1,String tag2,String tag3,String summary,String content);

    void debug(String content);
    void debug(String summary,String content);
    void debug(String tag,String summary,String content);
    void debug(String tag,String tag1,String summary,String content);
    void debug(String tag,String tag1,String tag2,String summary,String content);
    void debug(String tag,String tag1,String tag2,String tag3,String summary,String content);

    void info(String content);
    void info(String summary,String content);
    void info(String tag,String summary,String content);
    void info(String tag,String tag1,String summary,String content);
    void info(String tag,String tag1,String tag2,String summary,String content);
    void info(String tag,String tag1,String tag2,String tag3,String summary,String content);

    void warn(String content);
    void warn(String summary,String content);
    void warn(String tag,String summary,String content);
    void warn(String tag,String tag1,String summary,String content);
    void warn(String tag,String tag1,String tag2,String summary,String content);
    void warn(String tag,String tag1,String tag2,String tag3,String summary,String content);

    void error(Throwable content);
    void error(String summary,Throwable content);
    void error(String tag,String summary,Throwable content);
    void error(String tag,String tag1,String summary,Throwable content);
    void error(String tag,String tag1,String tag2,String summary,Throwable content);
    void error(String tag,String tag1,String tag2,String tag3,String summary,Throwable content);
}
