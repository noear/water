package org.slf4j.impl;

import org.noear.water.log.Level;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;


/**
 * Logger的单例工厂，读取日志系统配置，并对日志落盘行为进行统一管理
 */
public enum WaterLoggerFactory implements ILoggerFactory {
    /**
     * 工厂单例
     */
    INSTANCE;


    /**
     * 日志等级
     */
    private volatile Level level = Level.INFO;

    WaterLoggerFactory(){

    }

    @Override
    public Logger getLogger(String name) {
        return new WaterLogger(name);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }
}
