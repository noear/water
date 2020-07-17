package org.slf4j.impl;

import org.noear.water.WaterConfig;
import org.noear.water.log.WaterLogger;

public class Slf4jWaterWriterImp extends WaterLogger {

    public Slf4jWaterWriterImp() {
        super((String) null);
    }

    @Override
    public String getName() {
        return WaterConfig.water_logger_def();
    }

    @Override
    public void setName(String name) {
        //不许改
    }
}
