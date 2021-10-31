package org.noear.water.protocol;

import java.io.IOException;

/**
 * @author noear 2021/11/1 created
 */
public interface MsgBrokerFactory {
    /**
     * 更新日志源
     * */
    void updateBroker(String broker) throws IOException;

    /**
     * 获取日志源
     * */
    MsgBroker getBroker(String broker);
}
