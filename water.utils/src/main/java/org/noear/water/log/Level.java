package org.noear.water.log;

public enum Level {
    TRACE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5);

    public final int code;

    public static Level of(int code){
        switch (code){
            case 1:return TRACE;
            case 2:return DEBUG;
            case 3:return INFO;
            case 4:return WARN;
            case 5:return ERROR;
            default: return INFO;
        }
    }

    Level(int code) {
        this.code = code;
    }
}
