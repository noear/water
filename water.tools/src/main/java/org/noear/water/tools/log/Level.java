package org.noear.water.tools.log;


public enum Level {
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5);

    public final int code;

    public static Level of(int code){
        switch (code){
            case 1:return ERROR;
            case 2:return WARN;
            case 3:return INFO;
            case 4:return DEBUG;
            case 5:return TRACE;
            default: return INFO;
        }
    }

    Level(int code) {
        this.code = code;
    }
}
