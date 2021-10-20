package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.model.log.LogModel;

import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/10/20 created
 */
public class LogSourceEs implements LogSource {
    @Override
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception {
        return null;
    }

    @Override
    public void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime, String class_name, String thread_name) throws Exception {

    }

    @Override
    public void writeAll(String logger, List<LogEvent> list) throws Exception {

    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        return 0;
    }
}
