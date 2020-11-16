package waterapi.dso;

import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.log.Logger;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import waterapi.Config;

public class WaterLoggerLocal implements Logger {
    public static Logger get(String name) {
        return new WaterLoggerLocal(name);
    }

    public static Logger get(String name, Class<?> clz) {
        return new WaterLoggerLocal(name, clz.getName());
    }

    private String _name;
    private String _tag;

    public WaterLoggerLocal() {

    }

    public WaterLoggerLocal(String name) {
        _name = name;
    }

    public WaterLoggerLocal(String name, String tag) {
        this(name);
        _tag = tag;
    }

    public WaterLoggerLocal(String name, Class<?> clz) {
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
        writeDo(Level.TRACE, tag, tag1, tag2, tag3, summary, content);
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
        writeDo(Level.DEBUG, tag, tag1, tag2, tag3, summary, content);
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
        writeDo(Level.INFO, tag, tag1, tag2, tag3, summary, content);
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
        writeDo(Level.WARN, tag, tag1, tag2, tag3, summary, content);
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
        writeDo(Level.ERROR, tag, tag1, tag2, tag3, summary, content);
    }

    private void writeDo(Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        if (TextUtils.isEmpty(_name)) {
            return;
        }

        Datetime datetime = Datetime.Now();
        Context ctx = Context.current();
        LogEvent log = new LogEvent();

        log.logger = _name;
        if (ctx != null) {
            log.trace_id = ctx.header(WW.http_header_trace);
        }
        log.level = level.code;
        log.tag = tag;
        log.tag1 = tag1;
        log.tag2 = tag2;
        log.tag3 = tag3;
        log.summary = summary;
        log.content = content;
        log.from = Config.localHost;
        log.log_date = datetime.getDate();
        log.log_fulltime = datetime.getFulltime();

        LogPipelineLocal.singleton().add(log);
        //ProtocolHub.logStorer.write(_name, level, tag, tag1, tag2, tag3, summary, content, Config.localHost);
    }
}
