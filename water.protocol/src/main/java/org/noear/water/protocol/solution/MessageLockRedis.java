package org.noear.water.protocol.solution;

import org.noear.water.protocol.MessageLock;
import org.noear.water.utils.RedisX;

public class MessageLockRedis implements MessageLock {
    RedisX _redisX = null;
    int _seconds = 5;
    String _lockKey = "water_message_lock_";

    public MessageLockRedis(RedisX redisX) {
        _redisX = redisX;
    }

    @Override
    public boolean lock(String key) {
        String key2 = _lockKey + key;
        return _redisX.open1((ru) -> ru.key(key2).expire(_seconds).lock("_"));
    }

    @Override
    public void unlock(String key) {
        String key2 = _lockKey + key;
        _redisX.open0((ru) -> ru.key(key2).delete());
    }
}
