package org.noear.water.tools;


import java.util.Date;

//时间间隔
public class Timespan {
    private long interval = 0;

    public Timespan(Date date2) {
        if (date2 == null) {
            interval = 0;
        } else {
            interval = System.currentTimeMillis() - date2.getTime();
        }
    }

    public Timespan(Date date1, Date date2){
        interval = date1.getTime() - date2.getTime();
    }


    public Timespan(Datetime date2){
        interval = System.currentTimeMillis() - date2.getTicks();
    }


    public Timespan(Datetime date1, Datetime date2){
        interval = date1.getTicks() - date2.getTicks();
    }

    public Timespan(long time1, long time2) {
        interval = time1 - time2;
    }


    //@XNote("总毫秒数")
    public long milliseconds(){
        return interval;
    }

    //@XNote("总秒数")
    public long seconds(){
        return interval /1000;
    }

    //@XNote("总分钟")
    public long minutes(){
        return seconds()/60;
    }

    //@XNote("总小时")
    public long hours(){
        return minutes()/60;
    }

    //@XNote("总天数")
    public long days(){
        return hours()/24;
    }

}
