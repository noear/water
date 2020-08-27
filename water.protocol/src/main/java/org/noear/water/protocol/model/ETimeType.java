package org.noear.water.protocol.model;

public enum ETimeType {
    hour1(0),
    hour6(1),
    day1(2),
    day3(3),
    day7(4),
    day14(5),
    ;
    public int code;
    ETimeType(int code){
        this.code = code;
    }

    public static ETimeType of(int code){
        switch (code){
            case 1:return hour6;
            case 2:return day1;
            case 3:return day3;
            case 4:return day7;
            case 5:return day14;
            default:return hour1;
        }
    }
}
