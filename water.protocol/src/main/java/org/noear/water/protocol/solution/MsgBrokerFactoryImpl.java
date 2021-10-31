package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.MsgBroker;
import org.noear.water.protocol.MsgBrokerFactory;
import org.noear.weed.cache.ICacheServiceEx;

import java.io.IOException;

/**
 * @author noear 2021/11/1 created
 */
public class MsgBrokerFactoryImpl implements MsgBrokerFactory {
    MsgBroker _def;
    public MsgBrokerFactoryImpl(ConfigM def, ICacheServiceEx cache){
        _def = new MsgBrokerImpl(def, cache);
    }

    @Override
    public void updateBroker(String broker) throws IOException {

    }

    @Override
    public MsgBroker getBroker(String broker) {
        return _def;
    }
}
