package org.noear.water.protocol.solution;

import com.rabbitmq.client.*;
import org.noear.water.protocol.IMessageQueue;
import org.noear.water.utils.RabbitMQX;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Act1;

import java.util.HashMap;

/**
 * 基于 RabbitMQ 适配队列
 * */
public class MessageQueueRabbitMQ implements IMessageQueue {

    final RabbitMQX _rabbitX;

    /**
     * 交换器名称
     */
    final String rabbit_exchangeName = "water.message";
    /**
     * 路由KEY
     */
    final String rabbit_routingKey;
    /**
     * 队列名
     */
    final String rabbit_queueName;
    /**
     * 类型
     */
    final BuiltinExchangeType rabbit_type = BuiltinExchangeType.DIRECT;
    /**
     * 是否持久化
     */
    final boolean rabbit_durable = true;
    /**
     * 是否自动删除
     */
    final boolean rabbit_autoDelete = false;
    /**
     * 是否为内部
     */
    final boolean rabbit_internal = false;

    /**
     * 消息属性
     * */
    final AMQP.BasicProperties rabbit_msgProps;


    public MessageQueueRabbitMQ(String name, RabbitMQX rabbitX) {
        _rabbitX = rabbitX;

        rabbit_routingKey = name;
        rabbit_queueName = rabbit_exchangeName + "." + name;

        rabbit_msgProps = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();

        try {
            initDeclareAndBind();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initDeclareAndBind() throws Exception {
        _rabbitX.open0(channel -> {
            //1.声明交换机(String exchange:交换机名 , String type:交换机类型 , boolean durable:是否持久化 , boolean autoDelete:是否自动删除 , boolean internal:是否是内部交换机, Map arguments:交换机属性) throws IOException ;
            channel.exchangeDeclare(rabbit_exchangeName, rabbit_type, rabbit_durable, rabbit_autoDelete, rabbit_internal, new HashMap<>());

            //2.声明队列 (队列名, 是否持久化, 是否排他, 是否自动删除, 队列属性);
            channel.queueDeclare(rabbit_queueName, rabbit_durable, rabbit_autoDelete, false, new HashMap<>());

            //3.将队列Binding到交换机上 (队列名, 交换机名, Routing key, 绑定属性);
            channel.queueBind(rabbit_queueName, rabbit_exchangeName, rabbit_routingKey, new HashMap<>());
        });
    }

    @Override
    public boolean push(String msg) {
        _rabbitX.open0(channel -> {
            // 设置消息属性 发布消息 (exchange:交换机名, Routing key, props:消息属性, body:消息体);
            channel.basicPublish(rabbit_exchangeName, rabbit_routingKey, false, rabbit_msgProps, msg.getBytes());
        });
        return true;
    }

    @Override
    public String poll() {
        return _rabbitX.open1(channel -> {
            GetResponse rep = channel.basicGet(rabbit_queueName, true);

            if (rep == null) {
                return null;
            } else {
                return new String(rep.getBody());
            }
        });
    }

    @Override
    public void pollGet(Act1<String> callback) {
        _rabbitX.open0(channel -> {
            while (true) {
                GetResponse rep = channel.basicGet(rabbit_queueName, true);

                if (rep == null) {
                    break;
                }

                callback.run(new String(rep.getBody()));
            }
        });
    }

    @Override
    public long count() {
        return _rabbitX.open1(channel -> channel.messageCount(rabbit_queueName));
    }

    @Override
    public void close() throws Exception {

    }
}
