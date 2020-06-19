package org.noear.water.protocol.solution;

import com.rabbitmq.client.ConnectionFactory;
import org.noear.water.protocol.IMessageKeyBuilder;
import org.noear.water.protocol.IMessageQueue;

import java.util.Collection;

public class MessageQueueRocketMQ implements IMessageQueue {
    String _queue_name;

    ConnectionFactory factory;
    IMessageKeyBuilder keyBuilder = new MessageKeyBuilderDefault();


    @Override
    public void push(String msg) {

    }

    @Override
    public void pushAll(Collection<String> msgAry) {

    }

    @Override
    public String poll() {
        return null;
    }

    @Override
    public void remove(String msg) {

    }
}
