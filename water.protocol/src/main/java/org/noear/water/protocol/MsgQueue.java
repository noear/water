package org.noear.water.protocol;

import java.io.Closeable;
import java.util.function.Consumer;

/**
 * 消息队列接口（发到队列里的消息，是原消息ID）
 * */
public interface MsgQueue extends Closeable {
    /**
     * 添加一个消息
     * */
    boolean push(String msg_id) ;

    /**
     * 返问并移除队列头部的消息
     * */
    String poll();

    /**
     * 返问并移除队列头部的消息（以回调形式处理）
     * */
    void pollGet(Consumer<String> callback);

    /**
     * 数量
     * */
    long count();
}
