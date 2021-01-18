package org.slf4j.impl;

import org.noear.water.WaterSetting;
import org.noear.water.log.WaterLogger;

public class Slf4jWaterWriterImp extends WaterLogger {

    public Slf4jWaterWriterImp() {
        super((String) null);
    }

    @Override
    public String getName() {
        return WaterSetting.water_logger_def();
    }

    @Override
    public void setName(String name) {
        //不许改
    }
}
