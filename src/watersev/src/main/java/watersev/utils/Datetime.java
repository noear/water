package watersev.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Datetime implements Serializable {
    private Date     _datetime;
    private Calendar _calendar = null;

    public Datetime(Date date){
        setDate(date);
    }

    public Datetime(long milliseconds){
        setDate(new Date(milliseconds));
    }

    public Datetime setDate(Date date){
        _datetime = date;
        _calendar = Calendar.getInstance();
        _calendar.setTime(date);

        return this;
    }


    public static Datetime Now(){
        return new Datetime(new Date());
    }


    public  void addYear(int year) {
        doAdd(Calendar.YEAR, +year);
    }

    public  void addMonth(int month) {
        doAdd(Calendar.MONTH, +month);
    }

    public  void addDay(int day){
        doAdd(Calendar.DAY_OF_MONTH, +day);
    }

    public  void addHour(int hour){
        doAdd(Calendar.HOUR_OF_DAY, + hour);
    }

    public  void addMinute(int minute){
        doAdd(Calendar.MINUTE, + minute);
    }

    private void doAdd(int field, int amount){
        _calendar.add(field, + amount);

        _datetime = _calendar.getTime();
    }

    public int getYear(){
        return _calendar.get(Calendar.YEAR);
    }

    public int getMonth(){
        return _calendar.get(Calendar.MONTH);
    }

    public int getDays(){
        return _calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHours(){
        return _calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes(){
        return _calendar.get(Calendar.MINUTE);
    }

    public int getSeconds(){
        return _calendar.get(Calendar.SECOND);
    }

    public long getMilliseconds(){
        return _datetime.getTime();
    }

    public Date getDate(){
        return _datetime;
    }

    public String toString(String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(_datetime);
    }

    @Override
    public String toString(){
        return toString("yyyy-MM-dd HH:mm:ss");
    }

    //===================
    //
    public static Datetime parse(String datetime, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(datetime);
        return new Datetime(date);
    }

    public static Datetime tryParse(String datetime, String format)  {
        DateFormat df = new SimpleDateFormat(format);

        try {
            Date date = df.parse(datetime);
            return new Datetime(date);
        }catch (Exception ex){
            return null;
        }
    }

}
