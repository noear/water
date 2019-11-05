package org.noear.water.client.dso.log;

import org.noear.water.client.WaterClient;
import org.noear.water.client.dso.LogApi;
import org.noear.water.tools.AssertUtils;

public class WaterLogger implements Logger {
    public static Logger get(String name){
        return new WaterLogger(name);
    }



    private String _name;

    public WaterLogger(String name) {
        _name = name;

        AssertUtils.notEmpty(name, "name");
    }

    public String getName() {
        return _name;
    }

    @Override
    public void trace(Object content) {
        trace(null, null, null, null, null, content);
    }

    @Override
    public void trace(String summary, Object content) {
        trace(null, null, null, null, summary, content);
    }

    @Override
    public void trace(String tag, String summary, Object content) {
        trace(tag, null, null, null, summary, content);
    }

    @Override
    public void trace(String tag, String tag1, String summary, Object content) {
        trace(tag, tag1, null, null, summary, content);
    }

    @Override
    public void trace(String tag, String tag1, String tag2, String summary, Object content) {
        trace(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void trace(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        WaterClient.log.append(_name, Level.TRACE, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void debug(Object content) {
        debug(null, null, null, null, null, content);
    }

    @Override
    public void debug(String summary, Object content) {
        debug(null, null, null, null, summary, content);
    }

    @Override
    public void debug(String tag, String summary, Object content) {
        debug(tag, null, null, null, summary, content);
    }

    @Override
    public void debug(String tag, String tag1, String summary, Object content) {
        debug(tag, tag1, null, null, summary, content);
    }

    @Override
    public void debug(String tag, String tag1, String tag2, String summary, Object content) {
        debug(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void debug(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        WaterClient.log.append(_name, Level.DEBUG, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void info(Object content) {
        info(null, null, null, null, null, content);
    }

    @Override
    public void info(String summary, Object content) {
        info(null, null, null, null, summary, content);
    }

    @Override
    public void info(String tag, String summary, Object content) {
        info(tag, null, null, null, summary, content);
    }

    @Override
    public void info(String tag, String tag1, String summary, Object content) {
        info(tag, tag1, null, null, summary, content);
    }

    @Override
    public void info(String tag, String tag1, String tag2, String summary, Object content) {
        info(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void info(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        WaterClient.log.append(_name, Level.INFO, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void warn(Object content) {
        warn(null, null, null, null, null, content);
    }

    @Override
    public void warn(String summary, Object content) {
        warn(null, null, null, null, summary, content);
    }

    @Override
    public void warn(String tag, String summary, Object content) {
        warn(tag, null, null, null, summary, content);
    }

    @Override
    public void warn(String tag, String tag1, String summary, Object content) {
        warn(tag, tag1, null, null, summary, content);
    }

    @Override
    public void warn(String tag, String tag1, String tag2, String summary, Object content) {
        warn(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void warn(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        WaterClient.log.append(_name, Level.WARN, tag, tag1, tag2, tag3, summary, content);
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
        WaterClient.log.append(_name, Level.ERROR, tag, tag1, tag2, tag3, summary, content);
    }
}
