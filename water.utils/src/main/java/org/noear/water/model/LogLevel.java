package org.noear.water.model;

public enum LogLevel {
    TRACE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5);

    public final int code;

    public static LogLevel of(int code){
        for(LogLevel v : values()){
            if(v.code == code){
                return v;
            }
        }
        //默认值
        return INFO;
    }

    LogLevel(int code) {
        this.code = code;
    }
}
