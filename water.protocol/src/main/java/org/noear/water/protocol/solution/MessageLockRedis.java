package org.noear.water.protocol.solution;

import org.noear.water.protocol.IMessageLock;
import org.noear.water.utils.RedisX;

public class MessageLockRedis implements IMessageLock {
    RedisX _redisX = null;
    int _seconds = 30;

    public MessageLockRedis(RedisX redisX) {
        _redisX = redisX;
    }

    @Override
    public boolean lock(String key) {
        String key2 = "water_message_bus_" + key;
        return _redisX.open1((ru) -> ru.key(key2).expire(_seconds).lock("_"));
    }

    @Override
    public void unlock(String key) {
        String key2 = "water_message_bus_" + key;
        _redisX.open0((ru) -> ru.key(key2).delete());
    }
}
