package org.noear.water.protocol;

import java.util.Collection;

/**
 * 消息队列接口（发到队列里的消息，是原消息ID）
 * */
public interface IMessageQueue {
    /**
     * 添加一个消息
     * */
    void push(String msg) throws Exception;


    /**
     * 返问并移除队列头部的消息
     * */
    String poll() throws Exception;


    /**
     * 数量
     * */
    long count();
}
