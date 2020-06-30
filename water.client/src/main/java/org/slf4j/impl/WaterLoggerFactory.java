package org.slf4j.impl;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.noear.water.log.Level;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
