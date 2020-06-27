package org.noear.water.protocol;

public interface MessageLock {
    boolean lock(String key);

    void unlock(String key);
}
