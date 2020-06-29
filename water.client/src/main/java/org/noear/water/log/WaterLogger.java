package org.noear.water.log;

import com.lmax.disruptor.dsl.Disruptor;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;

public class WaterLogger implements Logger {
    public static WaterLogger get(String name) {
        return WaterLoggerFactory.INSTANCE.getLogger(name);
    }

    public static WaterLogger get(String name, Class<?> clz) {
        return WaterLoggerFactory.INSTANCE.getLogger(name, clz);
    }

    private String _name;
    private String _tag;
    private Disruptor<WaterLogEvent> _disruptor;

    public WaterLogger() {
        _disruptor = WaterLoggerFactory.INSTANCE.getDisruptor();
    }

    public WaterLogger( String name) {
        _disruptor = WaterLoggerFactory.INSTANCE.getDisruptor();
        _name = name;
    }

    public WaterLogger(String name, String tag) {
        this(name);
        _tag = tag;
    }

    public WaterLogger(String name, Class<?> clz) {
        this(name);
        _tag = clz.getSimpleName();
    }



    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public boolean isTraceEnabled() {
        return WaterLoggerFactory.INSTANCE.getLevel().code <= Level.TRACE.code;
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
        traceDo(tag, tag1, tag2, tag3, summary, content);
    }

    private void traceDo(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if(isTraceEnabled()) {
            appendDo(Level.TRACE, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return WaterLoggerFactory.INSTANCE.getLevel().code <= Level.DEBUG.code;
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
        debugDo(tag, tag1, tag2, tag3, summary, content);
    }

    private void debugDo(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if(isDebugEnabled()) {
            appendDo(Level.DEBUG, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return WaterLoggerFactory.INSTANCE.getLevel().code <= Level.INFO.code;
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
        infoDo(tag, tag1, tag2, tag3, summary, content);
    }

    private void infoDo(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if(isInfoEnabled()) {
            appendDo(Level.INFO, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return WaterLoggerFactory.INSTANCE.getLevel().code <= Level.WARN.code;
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
        warnDo(tag, tag1, tag2, tag3, summary, content);
    }

    private void warnDo(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if(isWarnEnabled()) {
            appendDo(Level.WARN, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return WaterLoggerFactory.INSTANCE.getLevel().code <= Level.ERROR.code;
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
        errorDo(tag, tag1, tag2, tag3, summary, content);
    }

    private void errorDo(String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if(isErrorEnabled()) {
            appendDo(Level.ERROR, tag, tag1, tag2, tag3, summary, content);
        }
    }

    private void appendDo(Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if(TextUtils.isEmpty(_name)){
            return;
        }

        long sequence = _disruptor.getRingBuffer().next();
        try {
            WaterLogEvent event = _disruptor.getRingBuffer().get(sequence);
            event.logger = _name;
            event.level = level;
            event.tag = tag;
            event.tag1 = tag1;
            event.tag2 = tag2;
            event.tag3 = tag3;
            event.summary = summary;
            event.content = content;
        } finally {
            _disruptor.getRingBuffer().publish(sequence);
        }
    }
}
