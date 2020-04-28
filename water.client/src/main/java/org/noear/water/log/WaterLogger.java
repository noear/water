package org.noear.water.log;

import org.noear.water.WaterClient;
import org.noear.water.utils.AssertUtils;

public class WaterLogger implements Logger {
    public static Logger get(String name){
        return new WaterLogger(name);
    }
    public static Logger get(String name, Class<?> clz) {
        return new WaterLogger(name, clz.getName());
    }

    private String _name;
    private String _tag;

    public WaterLogger(String name) {
        _name = name;
        AssertUtils.notEmpty(name, "name");
    }

    public WaterLogger(String name, String tag) {
        this(name);
        _tag = tag;
    }

    public WaterLogger(String name, Class<?> clz) {
        this(name);
        _tag = clz.getSimpleName();
    }

    public String getName() {
        return _name;
    }

    @Override
    public void trace(Object content) {
        trace(_tag, null, null, null, null, content);
    }

    @Override
    public void trace(String summary, Object content) {
        trace(_tag, null, null, null, summary, content);
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
        WaterClient.Log.append(_name, Level.TRACE, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void debug(Object content) {
        debug(_tag, null, null, null, null, content);
    }

    @Override
    public void debug(String summary, Object content) {
        debug(_tag, null, null, null, summary, content);
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
        WaterClient.Log.append(_name, Level.DEBUG, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void info(Object content) {
        info(_tag, null, null, null, null, content);
    }

    @Override
    public void info(String summary, Object content) {
        info(_tag, null, null, null, summary, content);
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
        WaterClient.Log.append(_name, Level.INFO, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void warn(Object content) {
        warn(_tag, null, null, null, null, content);
    }

    @Override
    public void warn(String summary, Object content) {
        warn(_tag, null, null, null, summary, content);
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
        WaterClient.Log.append(_name, Level.WARN, tag, tag1, tag2, tag3, summary, content);
    }

    @Override
    public void error(Object content) {
        error(_tag, null, null, null, null, content);
    }

    @Override
    public void error(String summary, Object content) {
        error(_tag, null, null, null, summary, content);
    }

    @Override
    public void error(String tag, String summary, Object content) {
        error(tag, null, null, null, summary, content);
    }

    @Override
    public void error(String tag, String tag1, String summary, Object content) {
        error(tag, tag1, null, null, summary, content);
    }

    @Override
    public void error(String tag, String tag1, String tag2, String summary, Object content) {
        error(tag, tag1, tag2, null, summary, content);
    }

    @Override
    public void error(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        WaterClient.Log.append(_name, Level.ERROR, tag, tag1, tag2, tag3, summary, content);
    }
}
