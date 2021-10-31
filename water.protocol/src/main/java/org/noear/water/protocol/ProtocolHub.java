package org.noear.water.protocol;

import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.solution.LogQuerierImpl;
import org.noear.water.protocol.solution.LogStorerImp;

/**
 * 协议中心
 * */
public final class ProtocolHub {
    //必填
    public static Config config;

    public static LogSourceFactory logSourceFactory;
    public static final LogQuerier logQuerier = new LogQuerierImpl();
    public static final LogStorer logStorer = new LogStorerImp();

    public static MsgBrokerFactory msgBrokerFactory;
    public static MsgSource getMsgSource(String broker){
        return msgBrokerFactory.getBroker(broker).getSource();
    }

//    public static MsgSource getMsgBroker(String broker){
//        return msgBrokerFactory.getBroker(broker).getSource();
//    }

    public static MsgQueue msgQueue;

    public static Monitoring monitoring;

    public static Heihei heihei;

    public static ConfigM cfg(String key) {
        return config.get(WW.water, key);
    }
}
