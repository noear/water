package watersev.dao;

import org.apache.http.util.TextUtils;
import watersev.models.water_msg.MessageModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuety on 2017/7/18.
 */
public class DisttimeUtil {
    //单位：0.1秒
    public static int nextTime(MessageModel msg) {
        double temp = getTimespan();

        switch (msg.dist_count){
            case 0:temp  = 0;break;
            case 1:temp  = temp+30;break;
            case 2:temp  = temp+2*60;break;
            case 3:temp  = temp+5*60;break;
            case 4:temp  = temp+10*60;break;
            case 5:temp  = temp+30*60;break;
            case 6:temp  = temp+60*60;break;
            case 7:temp  = temp+90*60;break;
            default:temp = temp+120*60;break;
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
