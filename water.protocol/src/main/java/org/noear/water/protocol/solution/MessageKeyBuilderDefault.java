package org.noear.water.protocol.solution;

import org.noear.water.protocol.IMessageKeyBuilder;

/**
 * 默认消息Key构建器
 * */
public class MessageKeyBuilderDefault implements IMessageKeyBuilder {
    @Override
    public String build(String msg) {
        return msg;
    }
}
