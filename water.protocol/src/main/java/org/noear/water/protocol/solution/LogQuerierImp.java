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
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, long timestamp) throws Exception {
        return ProtocolHub.logSourceFactory.getSource(logger)
                .query(logger, trace_id, level, size, tag, tag1, tag2, tag3, timestamp);
    }

    @Override
    public long clear(String logger) throws Exception {
        LoggerMeta mod = ProtocolHub.logSourceFactory.getLogger(logger);

        return ProtocolHub.logSourceFactory.getSource(logger)
                .clear(logger, mod.getKeepDays(), 0);
    }

    @Override
    public long clear(String logger, int keep_days, int limit_rows) throws Exception {
        return ProtocolHub.logSourceFactory.getSource(logger)
                .clear(logger, keep_days, limit_rows);
    }
}
