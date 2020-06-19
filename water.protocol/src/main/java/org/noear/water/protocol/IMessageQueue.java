package org.noear.water.protocol;

import java.util.Collection;

/**
 * 消息队列接口
 * */
public interface IMessageQueue {
    void push(String msg);
    void pushAll(Collection<String> msgAry);

    String pull();

    void remove(String msg);
    long count();
}
