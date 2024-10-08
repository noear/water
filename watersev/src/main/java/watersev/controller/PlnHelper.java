package watersev.controller;

import org.noear.luffy.task.cron.CronExpressionPlus;
import org.noear.luffy.task.cron.CronUtils;
import org.noear.water.utils.Datetime;
import watersev.models.water_paas.LuffyFileModel;

import java.text.ParseException;
import java.util.Date;

/**
 * 定时任务助手，用于构建下一个执行点
 *
 * @author noear
 */
public class PlnHelper {
    public static PlnNext getNextTimeByCron(LuffyFileModel task, Date baseTime) throws ParseException {
        PlnNext next = new PlnNext();

        CronExpressionPlus cron = CronUtils.get(task.plan_interval);

        next.datetime = cron.getNextValidTimeAfter(baseTime);


        //如果，限制特定的小时
        if (task.plan_state != 1) {
            Datetime now_time = Datetime.Now();
            Datetime nxt_time = new Datetime(next.datetime);

            if (cron.getHours().size() < 24) {
                next.allow = (nxt_time.getHours() == now_time.getHours());
            }

            //如果，限制特定的分
            if (next.allow && cron.getMinutes().size() < 60) {
                next.allow = (nxt_time.getMinutes() == now_time.getMinutes());
            }
        } else {
            next.allow = true;
        }

        return next;
    }

    public static PlnNext getNextTimeBySimple(LuffyFileModel task, Date baseTime) {
        PlnNext next = new PlnNext();

        Datetime begin_time = new Datetime(task.plan_begin_time);
        Datetime now_time = Datetime.Now();
        Datetime next_time = new Datetime(baseTime);

        //例：1s,1m,1h,1d
        String val_str = task.plan_interval.substring(0, task.plan_interval.length() - 1);
        int val = Integer.parseInt(val_str);
        String sty = task.plan_interval.substring(task.plan_interval.length() - 1);


        switch (sty) {
            case "s": //秒
                if (task.plan_last_timespan > (val * 1000)) {
                    //如果上次执行时长大于间隔，则用执行时长做为间隔
                    val = (int) (task.plan_last_timespan / 1000);
                }
                next_time.addSecond(val);
                break;
            case "m": //分
                if (task.plan_last_timespan > (val * 1000 * 60)) {
                    //如果上次执行时长大于间隔，则用执行时长做为间隔
                    val = (int) (task.plan_last_timespan / 1000 / 60);
                }
                next_time.setSecond(begin_time.getSeconds());
                next_time.addMinute(val);
                break;
            case "h": //时
                next_time.setMinute(begin_time.getMinutes());
                next_time.setSecond(begin_time.getSeconds());

                next_time.addHour(val);
                break;
            case "d": //日
                next.intervalOfDay = true;
                next.allow = (now_time.getHours() == begin_time.getHours());

                next_time.setHour(begin_time.getHours());
                next_time.setMinute(begin_time.getMinutes());
                next_time.setSecond(begin_time.getSeconds());

                next_time.addDay(val);
                break;
            default:
                next.allow = false; //不支持的格式，不允许执行
                next_time.addDay(1);
                break;
        }

        next.datetime = next_time.getFulltime();

        //1表示立即执行
        if (task.plan_state == 1) {
            next.allow = true;
        }

        return next;
    }
}
