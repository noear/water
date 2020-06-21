package org.noear.water.protocol.solution;

import org.noear.water.protocol.IMessageQueue;


public class MessageQueueRocketMQ implements IMessageQueue {
    String _queue_name;

    @Override
    public void push(String msg) {

    }

    @Override
    public String poll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

}
