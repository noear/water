package org.noear.water.protocol;

import org.noear.water.protocol.model.LoggerMeta;

public interface LogSourceFactory {
    LogSource getSource(String logger);
    LoggerMeta getLogger(String logger);
}
