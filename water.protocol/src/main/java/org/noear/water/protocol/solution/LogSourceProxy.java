package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.LogModel;

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
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception {
        return real.query(logger, trace_id, level, size, tag, tag1, tag2, tag3, log_date, log_id);
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime) throws Exception {
        real.write(log_id, logger, trace_id, level, tag, tag1, tag2, tag3, summary, content, from, log_fulltime);
    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) throws Exception {
        real.writeAll(logger, list);
    }

    @Override
    public long stat(String logger, Integer level, Integer log_date) throws Exception {
        return real.stat(logger, level, log_date);
    }

    @Override
    public void clear(String logger, int keep_days) {
        real.clear(logger, keep_days);
    }
}
