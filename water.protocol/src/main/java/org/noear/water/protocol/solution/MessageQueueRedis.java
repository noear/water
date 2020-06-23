package org.noear.water.protocol.solution;

import org.noear.water.protocol.IMessageQueue;
import org.noear.water.utils.RedisX;

/**
 * 基于 Redis 适配队列
 * */
public class MessageQueueRedis implements IMessageQueue {
    RedisX _redisX = null;
    String _queue_name;

    public MessageQueueRedis(String name, RedisX redisX) {
        _queue_name = name;
        _redisX = redisX;
    }

    @Override
    public boolean push(String msg) {
        _redisX.open0((rs) -> rs.key(_queue_name).listAdd(msg));
        return true;
    }


    @Override
    public String poll() {
        return _redisX.open1((rs) -> rs.key(_queue_name).listPop());
    }


    @Override
    public long count() {
        return _redisX.open1(rs -> rs.key(_queue_name).listLen());
    }

    @Override
    public void close() throws Exception {

    }
}
