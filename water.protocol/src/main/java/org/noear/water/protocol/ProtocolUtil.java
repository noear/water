package org.noear.water.protocol;

import org.noear.water.protocol.solution.MessageQueueRabbitMQ;
import org.noear.water.protocol.solution.MessageQueueRedis;
import org.noear.water.protocol.solution.MessageQueueRocketMQ;
import org.noear.water.utils.RabbitMQX;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;

import java.util.Properties;

class ProtocolUtil {
    public static IMessageQueue createMessageQueue(Properties prop){
        String name = prop.getProperty("queue.name","").toLowerCase();
        String type = prop.getProperty("queue.type","").toLowerCase();

        if(TextUtils.isEmpty(type) || TextUtils.isEmpty(type)){
            throw new RuntimeException("ProtocolHub::There was an error in the input configuration");
        }

        if("redis".equals(type)){
            //server
            //user
            //password
            //db
            return new MessageQueueRedis(name, new RedisX(prop));
        }

        if("rabbitmq".equals(type)){
            //server
            //user
            //password
            //virtualHost
            return new MessageQueueRabbitMQ(name, new RabbitMQX(prop));
        }

        if("rocketmq".equals(type)){
            //server
            return new MessageQueueRocketMQ(name, prop);
        }

        throw new RuntimeException("ProtocolHub::There was an error in the input configuration");
    }
}
