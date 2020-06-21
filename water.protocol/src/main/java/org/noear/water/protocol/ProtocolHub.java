package org.noear.water.protocol;

import org.noear.water.protocol.solution.MessageKeyBuilderDefault;

/**
 * 协议中心
 * */
public final class ProtocolHub {
    public static ILogQuerier logQuerier;
    public static ILogStorer logStorer;

    public static IMessageKeyBuilder messageKeyBuilder = new MessageKeyBuilderDefault();
    public static IMessageLock messageLock;
    public static IMessageQueue messageQueue;

    public static IHeihei heihei;
}
