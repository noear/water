package org.noear.water.protocol;

public interface IMessageLock {
    boolean lock(String key);

    void unlock(String key);
}
