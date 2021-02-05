package org.noear.water.protocol;

import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.solution.LogQuerierImp;
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

    public static IdBuilder idBuilder = null;

    public static LogSourceFactory logSourceFactory;
    public static final LogQuerier logQuerier = new LogQuerierImp();
    public static final LogStorer logStorer = new LogStorerImp();

    public static MessageSourceFactory messageSourceFactory;
    public static MessageSource messageSource(){
        return messageSourceFactory.getSource();
    }

    public static MessageLock messageLock;
    public static MessageQueue messageQueue;

    public static Monitoring monitoring;

    public static Heihei heihei;

    private static Map<String, MessageQueue> messageQueueMap = new HashMap<>();

    public static MessageQueue getMessageQueue(ConfigM cfg) {
        synchronized (messageQueueMap) {
            MessageQueue tmp = messageQueueMap.get(cfg.value);
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
