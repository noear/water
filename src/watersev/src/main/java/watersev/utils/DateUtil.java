package watersev.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtil {
    private static final int FIVE_MINUTE = 5;
    private static final int ONE_HOUR = 1;
    private static final int TWENTY_FOUR_HOUR = 24;
    private static final int THREE_DAY = 3;
    private static final int ONE_DAY = 1;

    public static String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }


    public static String format(Date time, String format) {
        if (time == null)
            return "";

        DateFormat df = new SimpleDateFormat(format);

        return df.format(time);
    }

    public static Date addDay(Date date, int day){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +day);

        return calendar.getTime();
    }

    public static Date addHour(Date date, int hour){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, + hour);

        return calendar.getTime();
    }

    public static int getDate(Date dateTime) {
        return Integer.parseInt(format(dateTime, "yyyyMMdd"));
    }

    public static int getHour(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
