package org.noear.water.protocol.solution;

import org.noear.redisx.RedisClient;
import org.noear.water.protocol.MessageQueue;

import java.util.function.Consumer;

/**
 * 基于 Redis 适配队列
 * */
public class MessageQueueRedis implements MessageQueue {
    RedisClient _redisX = null;
    String _queue_name;

    public MessageQueueRedis(String name, RedisClient redisX) {
        _queue_name = name;
        _redisX = redisX;
    }

    @Override
    public boolean push(String msg_id) {
        _redisX.open((rs) -> rs.key(_queue_name).listAdd(msg_id));
        return true;
    }


    @Override
    public String poll() {
        return _redisX.openAndGet((rs) -> rs.key(_queue_name).listPop());
    }

    @Override
    public void pollGet(Consumer<String> callback) {
        _redisX.open((rs) -> {
            while (true) {
                String msg = rs.key(_queue_name).listPop();

                if (msg == null) {
                    break;
                }

                callback.accept(msg);
            }
        });
    }


    @Override
    public long count() {
        return _redisX.openAndGet(rs -> rs.key(_queue_name).listLen());
    }

    @Override
    public void close() throws Exception {

    }
}
