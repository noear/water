package org.noear.water.log;

import com.lmax.disruptor.dsl.Disruptor;

/**
 * Logger的单例工厂，读取日志系统配置，并对日志落盘行为进行统一管理
 */
public enum WaterLoggerFactory {
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

    public Disruptor<WaterLogEvent> getDisruptor(){
        return disruptor;
    }

    public WaterLogger getLogger(String name) {
        return new WaterLogger( name);
    }

    public WaterLogger getLogger(String name, Class<?> clz) {
        return new WaterLogger(name, clz);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }
}
