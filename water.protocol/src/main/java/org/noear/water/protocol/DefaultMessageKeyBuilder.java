package org.noear.water.protocol;

/**
 * 默认消息Key构建器
 * */
public class DefaultMessageKeyBuilder implements IMessageKeyBuilder {
    @Override
    public String build(String msg) {
        return msg;
    }
}
