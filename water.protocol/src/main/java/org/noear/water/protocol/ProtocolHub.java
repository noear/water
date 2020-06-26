package org.noear.water.protocol;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.solution.ProtocolUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 协议中心
 * */
public final class ProtocolHub {
    public static IConfig config; //必填

    public static ILogSourceFactory logSourceFactory;
    public static ILogStorer logStorer;
    public static ILogQuerier logQuerier;

    public static IMessageLock messageLock;
    public static IMessageQueue messageQueue;

    public static IHeihei heihei;

    private static Map<String, IMessageQueue> messageQueueMap = new HashMap<>();

    public static IMessageQueue getMessageQueue(ConfigM cfg) {
        synchronized (messageQueueMap) {
            IMessageQueue tmp = messageQueueMap.get(cfg.value);
            if (tmp == null) {
                tmp = ProtocolUtil.createMessageQueue(cfg);
                messageQueueMap.put(cfg.value, tmp);
            }

            return tmp;
        }

    }
}
