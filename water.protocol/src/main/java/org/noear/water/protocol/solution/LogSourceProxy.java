package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;

import java.util.Date;
import java.util.List;

public class LogSourceProxy implements LogSource {
    LogSource real;

    public LogSourceProxy(ConfigM cfg) {
        if (cfg.value.indexOf("=mongodb") > 0) {
            real = new LogSourceMongo(cfg.getMg("water_log"));
        } else {
            real = new LogSourceRdb(cfg.getDb(true));
        }
    }

    @Override
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception {
        return real.query(logger, trace_id, level, size, tag, tag1, tag2, tag3, timestamp);
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime, String class_name, String thread_name) throws Exception {
        real.write(log_id, logger, trace_id, level, tag, tag1, tag2, tag3, summary, content, from, log_fulltime, class_name, thread_name);
    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) throws Exception {
        real.writeAll(logger, list);
    }

    @Override
    public long clear(String logger, int keep_days) throws Exception {
        return real.clear(logger, keep_days);
    }
}
