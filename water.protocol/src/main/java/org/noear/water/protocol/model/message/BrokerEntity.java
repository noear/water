package org.noear.water.protocol.model.message;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MsgBroker;

/**
 * @author noear 2021/11/1 created
 */
public class BrokerEntity {
    public MsgBroker source;
    public ConfigM sourceConfig;

    public BrokerEntity(MsgBroker source, ConfigM sourceConfig) {
        this.source = source;
        this.sourceConfig = sourceConfig;
    }
}
