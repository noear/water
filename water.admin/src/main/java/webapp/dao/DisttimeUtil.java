package webapp.dao;

import org.noear.water.tools.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DisttimeUtil {
    //单位：0.1秒
    public static int nextTime(int dist_count) {
        double temp = getTimespan();

        switch (dist_count){
            case 0:temp  = 0;break;
            case 1:temp  = temp+30;break; //30秒
            case 2:temp  = temp+2*60;break;//2分种
            case 3:temp  = temp+5*60;break;//5分钟
            case 4:temp  = temp+10*60;break;//10分钟
            case 5:temp  = temp+30*60;break;//30分钟
            case 6:temp  = temp+60*60;break;//1小时
            case 7:temp  = temp+90*60;break;//1.5小时
            default:temp = temp+120*60;break;//2小时
        }

        return (int) (temp /10);//秒/10
    }

    //单位：0.1秒
    public static int nextTime(Date date) {
        return (int) (getTimespan(date) / 10);
    }

    //单位：0.1秒
    public static int currTime() {
        return (int) (getTimespan() / 10);

    }

    //单位：0.1秒
    public static int currTime(int seconds) {
        return (int) (getTimespan() / 10) + (seconds * 10);

    }


    private static double getTimespan() {
        return (System.currentTimeMillis() / 1000.0) - 1500000000;
    }

    private static double getTimespan(Date date) {
        return (date.getTime() / 1000.0) - 1500000000;
    }

    // 1500000000000;
    // 1500000000
    // 1500370269918;

    public static Date getDate(String dateString, String format) {
        if(TextUtils.isEmpty(dateString))
            return null;

        SimpleDateFormat df = new SimpleDateFormat(format);

        try {
            return df.parse(dateString);
        } catch (Exception ex) {
            return null;
        }
    }
}
