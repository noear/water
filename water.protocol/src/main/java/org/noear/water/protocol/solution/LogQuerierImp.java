package org.noear.water.protocol.solution;

import org.noear.water.protocol.LogQuerier;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.LogModel;
import org.noear.water.protocol.model.LoggerMeta;

import java.util.List;

public class LogQuerierImp implements LogQuerier {
    public LogQuerierImp() {

    }

    @Override
    public List<LogModel> query(String logger, String trace_id, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception {
        return ProtocolHub.logSourceFactory.getSource(logger)
                .query(logger, trace_id, level, size, tag, tag1, tag2, tag3, log_date, log_id);
    }

    @Override
    public long stat(String logger, Integer level, Integer log_date) throws Exception {
        return ProtocolHub.logSourceFactory.getSource(logger)
                .stat(logger, level, log_date);
    }

    @Override
    public void clear(String logger) {
        LoggerMeta mod = ProtocolHub.logSourceFactory.getLogger(logger);

        ProtocolHub.logSourceFactory.getSource(logger)
                .clear(logger, mod.getKeepDays());
    }
}
