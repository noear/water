package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.LogSource;
import org.noear.water.protocol.MessageQueue;
import org.noear.water.utils.RabbitMQX;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;

import java.util.Properties;

public class ProtocolUtil {
    public static MessageQueue createMessageQueue(ConfigM cfg){
        Properties prop = cfg.getProp();

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

    public static LogSource createLogSource(ConfigM cfg){
        if (cfg == null || TextUtils.isEmpty(cfg.value)) {
            return null;
        }

        //String type = cfg.getProp().getProperty(WW.type_logger);
        return new LogSourceRdb(cfg.getDb(true));
    }
}
