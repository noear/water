package org.noear.water.protocol;

import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.solution.LogQuerierImpl;
import org.noear.water.protocol.solution.LogStorerImp;
import org.noear.water.protocol.solution.ProtocolUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 协议中心
 * */
public final class ProtocolHub {
    //必填
    public static Config config;

    public static LogSourceFactory logSourceFactory;
    public static final LogQuerier logQuerier = new LogQuerierImpl();
    public static final LogStorer logStorer = new LogStorerImp();

    public static MsgBroker msgSourceFactory;
    public static MsgSource msgSource(){
        return msgSourceFactory.getSource();
    }

    public static MsgQueue msgQueue;

    public static Monitoring monitoring;

    public static Heihei heihei;

    private static Map<String, MsgQueue> messageQueueMap = new HashMap<>();

    public static MsgQueue getMessageQueue(ConfigM cfg) {
        synchronized (messageQueueMap) {
            MsgQueue tmp = messageQueueMap.get(cfg.value);
            if (tmp == null) {
                tmp = ProtocolUtil.createMessageQueue(cfg);
                messageQueueMap.put(cfg.value, tmp);
            }

            return tmp;
        }

    }

    public static ConfigM cfg(String key) {
        return config.get(WW.water, key);
    }
}
