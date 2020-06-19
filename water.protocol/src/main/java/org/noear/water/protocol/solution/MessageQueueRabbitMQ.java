package org.noear.water.protocol.solution;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.GetResponse;
import org.noear.water.protocol.IMessageKeyBuilder;
import org.noear.water.protocol.IMessageQueue;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

public class MessageQueueRabbitMQ implements IMessageQueue {
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
    public String pop() {
        return null;
    }

    @Override
    public void remove(String msg) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists(String str) {
        return false;
    }
}
