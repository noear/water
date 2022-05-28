package org.noear.water.protocol;

import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.solution.LogQuerierImpl;
import org.noear.water.protocol.solution.LogStorerImp;

import java.util.List;

/**
 * 协议中心
 * */
public final class ProtocolHub {
    //必填
    public static Config config;

    public static LogSourceFactory logSourceFactory;

    public static LogSource getLogSource(String logger) {
        return logSourceFactory.getSource(logger);
    }

    public static final LogQuerier logQuerier = new LogQuerierImpl();
    public static final LogStorer logStorer = new LogStorerImp();

    public static MsgBrokerFactory msgBrokerFactory;

    public static List<MsgBroker> getMsgBrokerList() {
        return msgBrokerFactory.getBrokerList();
    }

    public static MsgBroker getMsgBroker(String broker) {
        return msgBrokerFactory.getBroker(broker);
    }

    public static MsgSource getMsgSource(String broker) {
        return getMsgBroker(broker).getSource();
    }

    public static Monitoring monitoring;

    public static HeiheiAgent heihei;

    public static ConfigM cfg(String key) {
        return config.get(WW.water, key);
    }
}
