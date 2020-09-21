package org.noear.water.protocol;

import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.model.LogModel;

import java.util.Date;
import java.util.List;

public interface LogSource {
    List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception;

    void write(long log_id, String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime);

    void writeAll(String logger, List<LogEvent> list);

    void clear(String logger, int keep_days);
}
