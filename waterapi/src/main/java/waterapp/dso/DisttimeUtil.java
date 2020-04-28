package waterapp.dso;


import waterapp.utils.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by noear on 2017/7/18.
 */
public class DisttimeUtil {
    //单位：0.1秒
    public static int nextTime(Date date) {
        return (int) (getTimespan(date) / 10);
    }

    //单位：0.1秒
    public static int currTime() {
        return (int) (getTimespan() / 10);
    }

    public static int currTime(int seconds) {
        return (int) (getTimespan() + seconds / 10);
    }

    private static double getTimespan() {
        return (System.currentTimeMillis() / 1000.0) - 1500000000;
    }

    private static double getTimespan(Date date) {
        return (date.getTime() / 1000.0) - 1500000000;
    }


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
