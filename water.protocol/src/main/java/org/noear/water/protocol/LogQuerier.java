package org.noear.water.protocol;

import org.noear.water.protocol.model.log.LogModel;

import java.util.List;

public interface LogQuerier {
    List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception;

    long clear(String logger) throws Exception;

    long clear(String logger, int keep_days) throws Exception;
}
