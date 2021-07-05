package watersev.controller;

import org.noear.luffy.task.cron.CronUtils;
import org.noear.water.utils.Datetime;
import watersev.models.water_paas.PaasFileModel;

import java.text.ParseException;
import java.util.Date;

/**
 * @author noear 2021/6/25 created
 */
public class PlnHelper {
    public static PlnNext getNextTimeByCron(PaasFileModel task, Date baseTime) throws ParseException {
        PlnNext plnNext = new PlnNext();

        plnNext.datetime = CronUtils.getNextTime(task.plan_interval, baseTime);
        plnNext.allow = true;

        return plnNext;
    }

    public static PlnNext getNextTimeBySimple(PaasFileModel task, Date baseTime) {
        PlnNext plnNext = new PlnNext();

        Datetime begin_time = new Datetime(task.plan_begin_time);
        Datetime next_time = new Datetime(baseTime);
        Datetime now_time = Datetime.Now();

        String s1 = task.plan_interval.substring(0, task.plan_interval.length() - 1);
        String s2 = task.plan_interval.substring(task.plan_interval.length() - 1);

        switch (s2) {
            case "s": //秒
                next_time.addSecond(Integer.parseInt(s1));
                break;
            case "m": //分
                next_time.setSecond(begin_time.getSeconds());
                next_time.addMinute(Integer.parseInt(s1));
                break;
            case "h": //时
                next_time.setMinute(begin_time.getMinutes());
                next_time.setSecond(begin_time.getSeconds());

                next_time.addHour(Integer.parseInt(s1));
                break;
            case "d": //日
                plnNext.intervalOfDay =true;
                plnNext.allow = (now_time.getHours() == begin_time.getHours());

                next_time.setHour(begin_time.getHours());
                next_time.setMinute(begin_time.getMinutes());
                next_time.setSecond(begin_time.getSeconds());

                next_time.addDay(Integer.parseInt(s1));
                break;
            default:
                plnNext.allow = false; //不支持的格式，不允许执行
                next_time.addDay(1);
                break;
        }

        plnNext.datetime = next_time.getFulltime();

        return plnNext;
    }
}
