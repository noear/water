package org.noear.water.protocol.solution;

import org.noear.water.protocol.IMessageKeyBuilder;
import org.noear.water.protocol.IMessageQueue;
import org.noear.water.utils.RedisX;

import java.util.Collection;

public class MessageQueueRedis implements IMessageQueue {
    RedisX _redisX = null;
    String _queue_name;
    IMessageKeyBuilder keyBuilder = new MessageKeyBuilderDefault();

    public MessageQueueRedis(String name, RedisX redisX) {
        _queue_name = name;
        _redisX = redisX;
    }

    @Override
    public void push(String msg) {
        _redisX.open0((rs) -> {
            String msg_key = keyBuilder.build(msg);
            String msg_key_h = _queue_name + "_" + msg_key;

            if (rs.key(msg_key_h).expire(30).lock()) {
                rs.key(_queue_name).listAdd(msg);
            }
        });
    }

    @Override
    public void pushAll(Collection<String> msgAry) {
        _redisX.open0((rs) -> {
            for (String msg : msgAry) {
                String msg_key = keyBuilder.build(msg);
                String msg_key_h = _queue_name + "_" + msg_key;

                if (rs.key(msg_key_h).expire(30).lock()) {
                    rs.key(_queue_name).listAdd(msg);
                }
            }
        });
    }

    @Override
    public String pop() {
        return _redisX.open1((rs) -> rs.key(_queue_name).listPop());
    }

    @Override
    public void remove(String msg) {
        String msg_key = keyBuilder.build(msg);
        String msg_key_h = _queue_name + "_" + msg_key;

        _redisX.open0((rs) -> rs.key(msg_key_h).delete());
    }

    @Override
    public long count() {
        return _redisX.open1(rs -> rs.key(_queue_name).listLen());
    }

    @Override
    public boolean exists(String msg) {
        return _redisX.open1((rs) -> {
            String msg_key = keyBuilder.build(msg);
            String msg_key_h = _queue_name + "_" + msg_key;

            return rs.key(msg_key_h).exists();
        });
    }
}
