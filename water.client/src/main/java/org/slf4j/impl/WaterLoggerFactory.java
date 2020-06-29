package org.slf4j.impl;

import com.lmax.disruptor.dsl.Disruptor;
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
     * 异步落盘线程的执行队列，使用了无锁内存队列进行日志事件的管理
     */
    private final Disruptor<WaterLogEvent> disruptor;

    /**
     * 日志等级
     */
    private volatile Level level = Level.INFO;

    WaterLoggerFactory(){
        try {
            disruptor = new Disruptor<>(WaterLogEvent::new, 1024, new WaterLogThreadFactory());
            disruptor.handleEventsWith(new WaterLogEventHandler());
            disruptor.start();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Logger getLogger(String name) {
        return new WaterLogger(name, disruptor);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }
}
