package org.noear.water.utils;

import java.util.Date;

public class DisttimeUtils {
    public static long currTime(){
        return System.currentTimeMillis();
    }

    public static long distTime(Date date){
        return date.getTime();
    }

    public static long nextTime(int dist_count) {
        int second = 0;

        switch (dist_count){
            case 0:second  = 2;break;//2秒
            case 1:second  = second+10;break; //10秒
            case 2:second  = second+30;break; //30秒
            case 3:second  = second+60;break; //1分钟
            case 4:second  = second+2*60;break;//2分种
            case 5:second  = second+5*60;break;//5分钟
            case 6:second  = second+10*60;break;//10分钟
            case 7:second  = second+30*60;break;//30分钟
            case 8:second  = second+60*60;break;//1小时
            default:second = second+120*60;break;//2小时
        }

        return System.currentTimeMillis() + (second * 1000);
    }

    public static Date parse(String dateStr){
        if(TextUtils.isEmpty(dateStr)){
            return null;
        }

        try{
            return Datetime.parse(dateStr,"yyyy-MM-dd HH:mm:ss").getFulltime();
        }catch (Exception ex){
            return null;
        }
    }
}
