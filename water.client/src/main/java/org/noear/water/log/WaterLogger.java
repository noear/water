package org.noear.water.log;

import org.noear.water.WaterClient;
import org.noear.water.dso.LogPipeline;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;

public class WaterLogger implements Logger {
    public static WaterLogger get(String name) {
        return new WaterLogger(name);
    }

    public static WaterLogger get(String name, Class<?> clz) {
        return new WaterLogger(name, clz);
    }

    private static volatile Level level = Level.TRACE;
    public static void setLevel(Level level){
        WaterLogger.level = level;
    }

    public static void setInterval(long interval){
        LogPipeline.singleton().setInterval(interval);
    }


    private String _name;
    private String _tag;

    public WaterLogger() {

    }

    public WaterLogger(String name) {
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
        return level.code <= Level.TRACE.code;
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
        if (isTraceEnabled()) {
            appendDo(Level.TRACE, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return level.code <= Level.DEBUG.code;
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
        if (isDebugEnabled()) {
            appendDo(Level.DEBUG, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return level.code <= Level.INFO.code;
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
        if (isInfoEnabled()) {
            appendDo(Level.INFO, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return level.code <= Level.WARN.code;
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
        if (isWarnEnabled()) {
            appendDo(Level.WARN, tag, tag1, tag2, tag3, summary, content);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return level.code <= Level.ERROR.code;
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
        if (isErrorEnabled()) {
            appendDo(Level.ERROR, tag, tag1, tag2, tag3, summary, content);
        }
    }

    private void appendDo(Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if (TextUtils.isEmpty(_name)) {
            return;
        }

        Datetime datetime = Datetime.Now();

        LogEvent log = new LogEvent();

        log.logger = _name;
        log.level = level.code;
        log.tag = tag;
        log.tag1 = tag1;
        log.tag2 = tag2;
        log.tag3 = tag3;
        log.summary = summary;
        log.content = content;
        log.from = WaterClient.localServiceAddr();
        log.log_date = datetime.getDate();
        log.log_fulltime = datetime.getFulltime();

        LogPipeline.singleton().add(log);
        //WaterClient.Log.append(_name, level, tag, tag1, tag2, tag3, summary, content, true);
    }
}
