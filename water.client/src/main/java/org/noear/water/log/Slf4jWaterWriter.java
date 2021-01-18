package org.noear.water.log;

import org.noear.water.log.Level;

public interface Slf4jWaterWriter {
    void write(String name, Level level, String content);
}
