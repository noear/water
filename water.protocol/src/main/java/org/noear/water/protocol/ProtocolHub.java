package org.noear.water.protocol;

import java.util.Properties;

/**
 * 协议中心
 * */
public final class ProtocolHub {
    public static ILogQuerier logQuerier;
    public static ILogStorer logStorer;

    public static IMessageLock messageLock;
    public static IMessageQueue messageQueue;

    public static IHeihei heihei;

    public static IMessageQueue createMessageQueue(Properties prop){
        return ProtocolUtil.createMessageQueue(prop);
    }
}
