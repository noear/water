package org.noear.water.protocol.solution;

import org.noear.water.protocol.LogQuerier;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.protocol.model.log.LoggerMeta;

import java.util.List;

public class LogQuerierImp implements LogQuerier {
    public LogQuerierImp() {

    }

    @Override
    public List<LogModel> query(String logger, Integer level, int size, String tagx, long timestamp) throws Exception {
        return ProtocolHub.logSourceFactory.getSource(logger)
                .query(logger, level, size, tagx, timestamp);
    }

    @Override
    public void create(String logger) throws Exception {
        ProtocolHub.logSourceFactory.getSource(logger)
                .create(logger);
    }

    @Override
    public long clear(String logger) throws Exception {
        LoggerMeta mod = ProtocolHub.logSourceFactory.getLoggerMeta(logger);

        return ProtocolHub.logSourceFactory.getSource(logger)
                .clear(logger, mod.getKeepDays(), 0);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        return ProtocolHub.logSourceFactory.getSource(logger)
                .clear(logger, keep_days, limit_rows);
    }
}
