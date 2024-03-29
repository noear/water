package org.noear.water.protocol.solution;

import org.noear.water.protocol.LogQuerier;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.protocol.model.log.LoggerMeta;

import java.util.List;

public class LogQuerierImpl implements LogQuerier {
    public LogQuerierImpl() {

    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long startLogId, long timestamp) throws Exception {
        return ProtocolHub.getLogSource(logger)
                .query(logger, level, size, tagx,startLogId, timestamp);
    }

    @Override
    public void create(String logger, int keep_days) throws Exception {
        ProtocolHub.getLogSource(logger)
                .create(logger, keep_days);
    }

    @Override
    public long clear(String logger) throws Exception {
        LoggerMeta mod = ProtocolHub.logSourceFactory.getLoggerMeta(logger);

        return ProtocolHub.getLogSource(logger)
                .clear(logger, mod.getKeepDays(), 0);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        return ProtocolHub.getLogSource(logger)
                .clear(logger, keep_days, limit_rows);
    }
}
