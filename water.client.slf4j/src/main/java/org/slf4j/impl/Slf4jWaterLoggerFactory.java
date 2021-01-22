package org.slf4j.impl;

import org.noear.water.WaterSetting;
import org.noear.water.log.Level;
import org.noear.water.log.WaterLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;


/**
 * Logger的单例工厂，读取日志系统配置，并对日志落盘行为进行统一管理
 */
public enum Slf4jWaterLoggerFactory implements ILoggerFactory {
    /**
     * 工厂单例
     */
    INSTANCE;


    /**
     * 日志等级（INFO 内容太多了）
     */
    private volatile Level level = Level.WARN;


    Slf4jWaterLoggerFactory() {

    }

    @Override
    public Logger getLogger(String name) {
        return new Slf4jWaterLogger(name);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }

    WaterLogger logger;

    public void write(String name, Level level, String content) {
        if (logger == null) {
            logger = new WaterLogger(WaterSetting.water_logger_def());
        }

        logger.slf4jWrite(level, name, content);
    }
}
