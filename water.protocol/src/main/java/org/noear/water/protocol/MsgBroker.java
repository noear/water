package org.noear.water.protocol;

/**
 * @author noear 2021/11/1 created
 */
public interface MsgBroker {
    /**
     * 获取队列
     * */
    MsgQueue getQueue();
    /**
     * 获取存储源
     * */
    MsgSource getSource();
}
