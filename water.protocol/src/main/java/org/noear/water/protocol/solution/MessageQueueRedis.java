package org.noear.water.protocol.solution;

import org.noear.water.protocol.MessageQueue;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.ext.Act1;

/**
 * 基于 Redis 适配队列
 * */
public class MessageQueueRedis implements MessageQueue {
    RedisX _redisX = null;
    String _queue_name;

    public MessageQueueRedis(String name, RedisX redisX) {
        _queue_name = name;
        _redisX = redisX;
    }

    @Override
    public boolean push(String msg_id) {
        _redisX.open0((rs) -> rs.key(_queue_name).listAdd(msg_id));
        return true;
    }


    @Override
    public String poll() {
        return _redisX.open1((rs) -> rs.key(_queue_name).listPop());
    }

    @Override
    public void pollGet(Act1<String> callback) {
        _redisX.open0((rs) -> {
            while (true) {
                String msg = rs.key(_queue_name).listPop();

                if (msg == null) {
                    break;
                }

                callback.run(msg);
            }
        });
    }


    @Override
    public long count() {
        return _redisX.open1(rs -> rs.key(_queue_name).listLen());
    }

    @Override
    public void close() throws Exception {

    }
}
