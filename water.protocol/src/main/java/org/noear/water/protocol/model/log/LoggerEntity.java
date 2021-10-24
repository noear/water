package org.noear.water.protocol.model.log;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;

/**
 * @author noear 2021/10/24 created
 */
public class LoggerEntity {
    public LogSource source;
    public ConfigM sourceConfig;

    public LoggerEntity(LogSource source, ConfigM sourceConfig) {
        this.source = source;
        this.sourceConfig = sourceConfig;
    }
}
