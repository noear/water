package org.noear.water.client.dso.log;

import org.noear.water.client.dso.LoggerApi;

import java.io.PrintWriter;
import java.io.StringWriter;

public class WaterLogger implements Logger {
    private String _name;

    public WaterLogger(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public void trace(String content) {
        trace(null, null, null, null, null, content);
    }

    @Override
    public void trace(String summary, String content) {
        trace(null, null, null, null, summary, content);
    }

    @Override
    public void trace(String tag, String summary, String content) {
        trace(tag, null, null, null, summary, content);
    }

    @Override
    public void trace(String tag, String tag1, String summary, String content) {
        trace(tag, tag1, null, null, summary, content);
    }

    @Override
    public void trace(String tag, String tag1, String tag2, String summary, String content) {
        trace(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void trace(String tag, String tag1, String tag2, String tag3, String summary, String content) {
        LoggerApi.append(_name, Level.TRACE, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void debug(String content) {
        debug(null, null, null, null, null, content);
    }

    @Override
    public void debug(String summary, String content) {
        debug(null, null, null, null, summary, content);
    }

    @Override
    public void debug(String tag, String summary, String content) {
        debug(tag, null, null, null, summary, content);
    }

    @Override
    public void debug(String tag, String tag1, String summary, String content) {
        debug(tag, tag1, null, null, summary, content);
    }

    @Override
    public void debug(String tag, String tag1, String tag2, String summary, String content) {
        debug(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void debug(String tag, String tag1, String tag2, String tag3, String summary, String content) {
        LoggerApi.append(_name, Level.DEBUG, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void info(String content) {
        info(null, null, null, null, null, content);
    }

    @Override
    public void info(String summary, String content) {
        info(null, null, null, null, summary, content);
    }

    @Override
    public void info(String tag, String summary, String content) {
        info(tag, null, null, null, summary, content);
    }

    @Override
    public void info(String tag, String tag1, String summary, String content) {
        info(tag, tag1, null, null, summary, content);
    }

    @Override
    public void info(String tag, String tag1, String tag2, String summary, String content) {
        info(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void info(String tag, String tag1, String tag2, String tag3, String summary, String content) {
        LoggerApi.append(_name, Level.INFO, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void warn(String content) {
        warn(null, null, null, null, null, content);
    }

    @Override
    public void warn(String summary, String content) {
        warn(null, null, null, null, summary, content);
    }

    @Override
    public void warn(String tag, String summary, String content) {
        warn(tag, null, null, null, summary, content);
    }

    @Override
    public void warn(String tag, String tag1, String summary, String content) {
        warn(tag, tag1, null, null, summary, content);
    }

    @Override
    public void warn(String tag, String tag1, String tag2, String summary, String content) {
        warn(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void warn(String tag, String tag1, String tag2, String tag3, String summary, String content) {
        LoggerApi.append(_name, Level.WARN, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void error(Throwable content) {
        error(null, null, null, null, null, content);
    }

    @Override
    public void error(String summary, Throwable content) {
        error(null, null, null, null, summary, content);
    }

    @Override
    public void error(String tag, String summary, Throwable content) {
        error(tag, null, null, null, summary, content);
    }

    @Override
    public void error(String tag, String tag1, String summary, Throwable content) {
        error(tag, tag1, null, null, summary, content);
    }

    @Override
    public void error(String tag, String tag1, String tag2, String summary, Throwable content) {
        error(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void error(String tag, String tag1, String tag2, String tag3, String summary, Throwable content) {
        LoggerApi.append(_name, Level.ERROR, tag, tag1, tag2, tag3, summary, content);
    }
}
