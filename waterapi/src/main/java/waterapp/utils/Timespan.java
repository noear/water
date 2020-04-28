package waterapp.utils;

import java.util.Date;

//时间间隔
public class Timespan {
    private long interval = 0;

    public Timespan(Date date2){
        interval = System.currentTimeMillis() - date2.getTime();
    }

    public Timespan(Datetime date2){
        interval = System.currentTimeMillis() - date2.getTicks();
    }

    public Timespan(Date date1, Date date2){
        interval = date1.getTime() - date2.getTime();
    }

    public Timespan(Datetime date1, Datetime date2){
        interval = date1.getTicks() - date2.getTicks();
    }

    public long milliseconds(){
        return interval;
    }

    //相差秒数
    public long seconds(){
        return interval /1000;
    }

    //相差分钟
    public long minutes(){
        return seconds()/60;
    }

    //相差小时
    public long hours(){
        return minutes()/60;
    }

    //相差天数
    public long days(){
        return hours()/24;
    }

}
