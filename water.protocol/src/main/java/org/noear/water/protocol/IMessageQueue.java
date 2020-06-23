package org.noear.water.protocol;

/**
 * 消息队列接口（发到队列里的消息，是原消息ID）
 * */
public interface IMessageQueue extends AutoCloseable {
    /**
     * 添加一个消息
     * */
    boolean push(String msg) ;


    /**
     * 返问并移除队列头部的消息
     * */
    String poll();


    /**
     * 数量
     * */
    long count();
}
