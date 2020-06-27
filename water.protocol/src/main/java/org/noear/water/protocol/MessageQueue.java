package org.noear.water.protocol;

import org.noear.water.utils.ext.Act1;

/**
 * 消息队列接口（发到队列里的消息，是原消息ID）
 * */
public interface MessageQueue extends AutoCloseable {
    /**
     * 添加一个消息
     * */
    boolean push(String msg) ;


    /**
     * 返问并移除队列头部的消息
     * */
    String poll();

    /**
     * 返问并移除队列头部的消息（以回调形式处理）
     * */
    void pollGet(Act1<String> callback);

    /**
     * 数量
     * */
    long count();
}
