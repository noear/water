package org.slf4j.impl;

import org.noear.water.log.Level;

public interface Slf4jWaterWriter {
    void write(String name, Level level, String content);
}
