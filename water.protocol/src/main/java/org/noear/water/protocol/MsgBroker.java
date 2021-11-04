package org.noear.water.protocol;

import java.io.Closeable;

/**
 * @author noear 2021/11/1 created
 */
public interface MsgBroker extends Closeable {
    /**
     * 获取名字
     * */
    String getName();

    /**
     * 获取队列
     * */
    MsgQueue getQueue();
    /**
     * 获取存储源
     * */
    MsgSource getSource();
}
