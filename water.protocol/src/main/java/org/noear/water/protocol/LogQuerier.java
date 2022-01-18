package org.noear.water.protocol;

import org.noear.water.protocol.model.log.LogModel;

import java.util.List;

public interface LogQuerier {
    List<LogModel> query(String logger, Integer level, int size, String tagx, long startLogId, long timestamp) throws Exception;

    void create(String logger, int keep_days) throws Exception;

    long clear(String logger) throws Exception;

    long clear(String logger, int keep_days, int limit_rows) throws Exception;
}
