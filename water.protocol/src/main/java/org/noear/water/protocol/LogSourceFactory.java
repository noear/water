package org.noear.water.protocol;

import org.noear.water.protocol.model.log.LoggerMeta;

import java.io.IOException;

public interface LogSourceFactory {
    /**
     * 更新日志源
     * */
    void updateSource(String logger) throws IOException;

    /**
     * 获取日志源
     * */
    LogSource getSource(String logger);

    /**
     * 获取日志器元信息
     * */
    LoggerMeta getLoggerMeta(String logger);
}
