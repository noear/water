package webapp.utils;

import java.util.Date;

public class TimeUtil {
    public static final String liveTimeFormat(Date time) {
        if (time == null)
            time = new Date();

        return DateUtil.format(time, "yyyy-MM-dd HH:mm");
    }
}
