package org.noear.water.protocol.solution;

import com.rabbitmq.client.*;
import org.noear.water.protocol.IMessageKeyBuilder;
import org.noear.water.protocol.IMessageQueue;
import org.noear.water.protocol.solution.rabbitmq.ChannelUtils;

import java.util.HashMap;

/**
 *
 *
 * 1.queue ：存储消息的队列，可以指定name来唯一确定
 *
 * 2.exchange：交换机（常用有三种），用于接收生产者发来的消息，并通过binding-key 与 routing-key 的匹配关系来决定将消息分发到指定queue
 *
 *  2.1 Direct（路由模式）：完全匹配 > 当消息的routing-key 与 exchange和queue间的binding-key完全匹配时，将消息分发到该queue
 *
 *  2.2 Fanout （订阅模式）：与binding-key和routing-key无关，将接受到的消息分发给有绑定关系的所有队列（不论binding-key和routing-key是什么）
 *
 *  2.3 Topic （通配符模式）：用消息的routing-key 与 exchange和queue间的binding-key 进行模式匹配，当满足规则时，分发到满足规则的所有队列
 *
 * */
public class MessageQueueRabbitMQ implements IMessageQueue {

    final IMessageKeyBuilder keyBuilder;
    final AMQP.BasicProperties basicProperties;
    final String exchangeName = "rabbitmq.wj";
    final String routingKey = "add";
    final String queueKey = "rabbitmq.wj.add";

    public MessageQueueRabbitMQ() {
        keyBuilder = new MessageKeyBuilderDefault();
        basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
    }

    @Override
    public void push(String msg) throws Exception {
        Channel channel = ChannelUtils.getChannelInstance("队列消息生产者");

        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());

        // 设置消息属性 发布消息 (交换机名, Routing key, 可靠消息相关属性 后续会介绍, 消息属性, 消息体);
        channel.basicPublish(exchangeName, routingKey, false, basicProperties, msg.getBytes());

    }

    @Override
    public String poll() throws Exception {
        Channel channel = ChannelUtils.getChannelInstance("队列消息消费者");

        // 声明队列 (队列名, 是否持久化, 是否排他, 是否自动删除, 队列属性);
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(queueKey, true, false, false, new HashMap<>());

        // 声明交换机 (交换机名, 交换机类型, 是否持久化, 是否自动删除, 是否是内部交换机, 交换机属性);
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());

        // 将队列Binding到交换机上 (队列名, 交换机名, Routing key, 绑定属性);
        channel.queueBind(declareOk.getQueue(), exchangeName, routingKey, new HashMap<>());


        GetResponse rep = channel.basicGet(declareOk.getQueue(), true);

        return new String(rep.getBody());
    }


    @Override
    public long count() {
        return 0;
    }
}
