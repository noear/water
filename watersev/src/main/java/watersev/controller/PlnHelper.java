package watersev.controller;

import org.noear.luffy.task.cron.CronUtils;
import org.noear.water.utils.Datetime;
import watersev.models.water_paas.PaasFileModel;

import java.util.Date;

/**
 * @author noear 2021/6/25 created
 */
public class PlnHelper {
    public static Date getNextTimeByCron(PaasFileModel task, Date baseTime) {
        return CronUtils.getNextTime(task.plan_interval, baseTime);
    }

    public static Date getNextTimeBySimple(PaasFileModel task, Date baseTime) {
        Datetime begin_time = new Datetime(task.plan_begin_time);
        Datetime next_time = new Datetime(baseTime);

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
                task._is_day_task = true;
                next_time.setHour(begin_time.getHours());
                next_time.setMinute(begin_time.getMinutes());
                next_time.setSecond(begin_time.getSeconds());

                next_time.addDay(Integer.parseInt(s1));
                break;
            case "M": //月
                task._is_day_task = true;
                next_time.setHour(begin_time.getHours());
                next_time.setMinute(begin_time.getMinutes());
                next_time.setSecond(begin_time.getSeconds());

                next_time.addMonth(Integer.parseInt(s1));
                break;
            default:
                next_time.addDay(1);
                break;
        }

        return next_time.getFulltime();
    }
}
