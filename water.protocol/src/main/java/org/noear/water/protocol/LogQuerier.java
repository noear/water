package org.noear.water.protocol;

import org.noear.water.protocol.model.log.LogModel;

import java.util.List;

public interface LogQuerier {
    List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception;

    long stat(String logger, Integer level, Integer log_date) throws Exception;

    void clear(String logger);
}
