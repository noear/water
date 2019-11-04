package org.noear.water.client.dso.log;

public enum Level {
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5);

    public final int code;

    Level(int code) {
        this.code = code;
    }
}
