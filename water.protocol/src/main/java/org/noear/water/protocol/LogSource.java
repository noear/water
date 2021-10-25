package org.noear.water.protocol;

import org.noear.water.model.LogM;
import org.noear.water.protocol.model.log.LogModel;

import java.io.Closeable;
import java.util.List;

public interface LogSource extends Closeable {
    List<LogModel> query(String logger, Integer level, int size, String tagx, long timestamp) throws Exception;

    void writeAll(String logger, List<LogM> list) throws Exception;

    void create(String logger) throws Exception;

    long clear(String logger, int keep_days, int limit_rows) throws Exception;
}
