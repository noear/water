package org.slf4j.impl;

import org.noear.water.WaterSetting;
import org.noear.water.log.Level;
import org.noear.water.log.WaterLogger;

public class Slf4jWaterWriterImp implements Slf4jWaterWriter {
    WaterLogger logger;

    public Slf4jWaterWriterImp() {
    }

    @Override
    public void write(String name, Level level, String content) {
        if (logger == null) {
            logger = new WaterLogger(WaterSetting.water_logger_def());
        }

        logger.slf4jWrite(level, name, content);
    }
}
