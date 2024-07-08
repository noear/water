package demo;

import org.junit.jupiter.api.Test;
import org.noear.luffy.task.cron.CronExpression;
import org.noear.luffy.task.cron.CronExpressionPlus;
import org.noear.luffy.task.cron.CronUtils;
import org.noear.water.utils.Datetime;

import java.util.Date;

/**
 * @author noear 2021/6/25 created
 */
public class PlnTest {
    @Test
    public void test1() throws Exception{
        Datetime datetime = Datetime.parse("2021-05-05 12:12", "yyyy-MM-dd HH:mm");

        Date nexttime = CronUtils.getNextTime("* * * * * ?", datetime.getFulltime());

        System.out.println(new Datetime(nexttime).toString("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void test2() throws Exception{
        Datetime datetime = Datetime.parse("2021-05-05 12:12", "yyyy-MM-dd HH:mm");

        Date nexttime = CronUtils.getNextTime("* * 1 * * ?", datetime.getFulltime());

        System.out.println(new Datetime(nexttime).toString("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void test3() throws Exception{
        CronExpression cron = CronUtils.get("* * 1 * * ?");
        System.out.println(cron);

        cron = CronUtils.get("0 * * * * ?");
        System.out.println(cron);

        cron = CronUtils.get("0 * 1,5 * * ?");
        System.out.println(cron);
    }

    @Test
    public void test4() throws Exception{
        CronExpressionPlus cron = CronUtils.get("0 0 11 * * ? +05");

        assert cron.getHours().size() == 1;
    }
}
