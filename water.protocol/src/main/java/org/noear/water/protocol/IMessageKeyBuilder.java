package org.noear.water.protocol;

/**
 * 消息主键构建器接口
 * */
@FunctionalInterface
public interface IMessageKeyBuilder {
    /**
     * 构建消息主键
     * */
    String build(String msg);
}
