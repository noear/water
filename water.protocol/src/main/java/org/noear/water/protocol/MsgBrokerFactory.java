package org.noear.water.protocol;

import org.noear.water.protocol.model.message.BrokerMeta;

import java.io.IOException;
import java.util.List;

/**
 * @author noear 2021/11/1 created
 */
public interface MsgBrokerFactory {


    /**
     * 更新消息管道
     * */
    void updateBroker(String broker) throws IOException;

    /**
     * 获取消息管道
     * */
    MsgBroker getBroker(String broker);

    /**
     * 获取消息管道
     *
     * @return*/
    List<MsgBroker> getBrokerList();


    /**
     * 获取日志器元信息
     * */
    BrokerMeta getBrokerMeta(String broker);
}
