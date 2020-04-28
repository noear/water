package waterapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    private static final int FIVE_MINUTE = 5;
    private static final int ONE_HOUR = 1;
    private static final int TWENTY_FOUR_HOUR = 24;
    private static final int THREE_DAY = 3;
    private static final int ONE_DAY = 1;

    public static String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }


    public static Date get_Overdue_Date(Date date ,int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, day);
        Date overdue_date = cal.getTime();
        return overdue_date;
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

    public static String addDayStr(Date date, int day){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime());
    }

    public static int getDate(Date dateTime) {
        return Integer.parseInt(format(dateTime, "yyyyMMdd"));
    }

    public static int getHour(Date dateTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date dateTime){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);

        return calendar.get(Calendar.MINUTE);

    }




    public static String dateTimeOfPublic(Date date) {

        Instant time = date.toInstant();
        Instant now = Instant.now();

        long min = time.until(now, ChronoUnit.MINUTES);
        long hour = time.until(now, ChronoUnit.HOURS);

        long day = time.truncatedTo(ChronoUnit.DAYS)
                .until(now.truncatedTo(ChronoUnit.DAYS), ChronoUnit.DAYS);

        String dateTime = "";

        if (min <= FIVE_MINUTE) {

            dateTime = "刚刚";

        } else if (hour < ONE_HOUR) {

            dateTime = min + "分钟前";

        } else if (hour <= TWENTY_FOUR_HOUR) {

            dateTime = hour + "小时前";

        } else if (day == ONE_DAY) {

            dateTime = "昨天";

        } else if (day <= THREE_DAY) {

            dateTime = day + "天前";

        } else {

            dateTime = LocalDateTime.ofInstant(time, ZoneId.of("Asia/Shanghai"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        }

        return dateTime;

    }


    public static Date getDelay(int Millisecond){
       return new Date(System.currentTimeMillis()+Millisecond);
    }

    /**
     * 字符串转换成日期
     * @param strData 字符串时间
     * @param formatStr 格式化格式
     * @return date
     */
    public static Date StrToDate(String strData, String formatStr ) {
        //"yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(strData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    //用于查询时，若为结束时间需加一天
    public static Date getEndAddDate(String endDate){
        Date endDay=StrToDate(endDate,"yyyy-MM-dd");
        return addDay(endDay,1);
    }
}
