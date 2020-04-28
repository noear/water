package org.noear.water.protocol;

/**
 * 消息主键构建器
 * */
@FunctionalInterface
public interface IMessageKeyBuilder {
    String build(String msg);
}
