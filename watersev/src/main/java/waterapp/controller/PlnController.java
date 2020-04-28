package waterapp.controller;

import org.noear.solon.annotation.XBean;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.Timecount;
import org.noear.water.utils.Timespan;
import solonjt.JtRun;
import waterapp.dso.*;
import waterapp.dso.db.DbWaterPaasApi;
import waterapp.models.water_paas.PaasFileModel;

import java.util.Date;
import java.util.List;

@XBean
public class PlnController implements IJob {
    @Override
    public String getName() {
        return "pln";
    }

    @Override
    public int getInterval() {
        return 1000 * 60;
    }


    private boolean _init = false;

    @Override
    public void exec() throws Exception {
        if (_init == false) {
            _init = true;

            DbWaterPaasApi.resetPlanState();
        }

        List<PaasFileModel> list = DbWaterPaasApi.getPlanList();

        for (PaasFileModel task : list) {
            new Thread(() -> {
                doExec(task);
            }).start();
        }
    }

    private void doExec(PaasFileModel task) {
        try {
            runTask(task);
        } catch (Exception ex) {
            ex.printStackTrace();

            try {
                DbWaterPaasApi.setPlanState(task, 8, "error");
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

            LogUtil.error(this, task.file_id + "", "", ex);

            AlarmUtil.tryAlarm(task);
        }
    }

    private void runTask(PaasFileModel task) throws Exception {


        //1.1.检查次数
        if (task.plan_max > 0 && task.plan_count >= task.plan_max) {
            return;
        }

        //1.2.检查重复间隔
        if (task.plan_interval == null || task.plan_interval.length() < 2) {
            return;
        }

        //1.3.检查是否在处理中
        if (task.plan_state == 2) {
            return;
        }

        //1.4.检查时间
        Date temp = task.plan_last_time;
        if (temp == null) {
            temp = task.plan_begin_time;
        }

        if (temp == null) {
            return;
        }

        //1.5.检查执行时间是否到了
        {
            Datetime last_time = new Datetime(temp);

            String s1 = task.plan_interval.substring(0, task.plan_interval.length() - 1);
            String s2 = task.plan_interval.substring(task.plan_interval.length() - 1);

            switch (s2) {
                case "m":
                    last_time.addMinute(Integer.parseInt(s1));
                    break;
                case "h":
                    last_time.addHour(Integer.parseInt(s1));
                    break;
                case "d":
                    task._is_day_task = true;
                    last_time.addDay(Integer.parseInt(s1));
                    break;
                case "M":
                    task._is_day_task = true;
                    last_time.addMonth(Integer.parseInt(s1));
                    break;
                default:
                    last_time.addDay(1);
                    break;
            }

            //1.5.2.如果未到执行时间则反回
            if (new Timespan(last_time.getFulltime()).seconds() < 0) {
                return;
            }
        }

        //////////////////////////////////////////

        //计时开始
        Timecount timecount = new Timecount().start();

        //2.执行
        do_runTask(task);

        //计时结束
        long timespan = timecount.stop().milliseconds();

        WaterClient.Track.track("water-plan", task.tag, task.path, timespan);
    }

    private void do_runTask(PaasFileModel task) throws Exception {


        //开始执行::
        task.plan_last_time = new Date();
        DbWaterPaasApi.setPlanState(task, 2, "processing");

        //2.执行
        try {
            JtRun.exec(task);
        } catch (Exception ex) {
            throw ex;
        }


        //3.更新状态
        task.plan_count = task.plan_count + 1;
        DbWaterPaasApi.setPlanState(task, 9, "OK");

        LogUtil.writeForPlan(this, task);
    }
}
