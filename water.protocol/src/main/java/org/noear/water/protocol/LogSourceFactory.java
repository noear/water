package org.noear.water.protocol;

import org.noear.water.protocol.model.LoggerModel;

public interface LogSourceFactory {
    LogSource getSource(String logger);
    LoggerModel getLogger(String logger);
}
