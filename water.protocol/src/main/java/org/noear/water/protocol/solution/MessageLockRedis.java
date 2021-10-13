package org.noear.water.protocol.solution;

import org.noear.redisx.RedisClient;
import org.noear.water.protocol.MessageLock;

public class MessageLockRedis implements MessageLock {
    RedisClient _redisX = null;
    int _seconds = 5;
    String _lockKey = "water_message_lock_";

    public MessageLockRedis(RedisClient redisX) {
        _redisX = redisX;
    }

    @Override
    public boolean lock(String key) {
        String key2 = _lockKey + key;
        return _redisX.openAndGet((ru) -> ru.key(key2).expire(_seconds).lock("_"));
    }

    @Override
    public void unlock(String key) {
        String key2 = _lockKey + key;
        _redisX.open((ru) -> ru.key(key2).delete());
    }
}
