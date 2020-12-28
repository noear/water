package org.noear.water.dso;

import org.noear.water.model.MessageM;

/**
 * 消息处理接口
 *
 * <p><code>
 * @WaterMessage("test.hello")
 * public class TestMessage implements MessageHandler {
 *     @Override
 *     public boolean handler(MessageM msg) throws Throwable {
 *         return true;
 *     }
 * }
 * </code></p>
 *
 * @author noear
 * @since 2.0
 * */
public interface MessageHandler {
    boolean handler(MessageM msg) throws Throwable;
}
