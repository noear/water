package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.integration.solon.WaterAdapter;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.Timecount;
import org.noear.water.utils.Timespan;
import luffy.JtRun;
import watersev.dso.*;
import watersev.dso.db.DbWaterPaasApi;
import watersev.models.water_paas.PaasFileModel;
import watersev.utils.CallUtil;

import java.util.Date;
import java.util.List;

/**
 * 成功:9
 * 出错:8
 * 处理中:2
 * 待处理:0
 *
 * */
@Component
public class PlnController implements IJob {
    @Override
    public String getName() {
        return "pln";
    }

    @Override
    public int getDelay() {
        return 1000;
    }

    @Override
    public int getInterval() {
        return 1000;
    }


    private boolean _init = false;

    @Override
    public void exec() throws Exception {
        if (_init == false) {
            _init = true;

            DbWaterPaasApi.resetPlanState();
        }

        JtRun.initAwait();

        List<PaasFileModel> list = DbWaterPaasApi.getPlanList();

        System.out.println("查到任务数：" + list.size());

        for (PaasFileModel task : list) {
            //加锁，以支持多节点处理
            if (LockUtils.tryLock("waterplan", task.file_id + "_job")) {
                CallUtil.asynCall(() -> {
                    doExec(task);
                });
            }
        }
    }

    private void doExec(PaasFileModel task) {

        //2.1.计时开始
        Timecount timecount = new Timecount().start();

        try {
            ContextEmpty ctx = new ContextEmpty();
            ContextUtil.currentSet(ctx);
            ctx.attrSet("file", task);

            runTask(task, timecount);
        } catch (Exception ex) {
            long _times = timecount.stop().milliseconds();

            ex.printStackTrace();

            try {
                DbWaterPaasApi.setPlanState(task, 8, "error");
            } catch (Throwable ex2) {
                ex2.printStackTrace();
            }


            LogUtil.planError(this, task, _times, ex);

            AlarmUtil.tryAlarm(task);
        } finally {
            ContextUtil.currentRemove();
        }
    }

    private void runTask(PaasFileModel task, Timecount timecount) throws Exception {


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
            Datetime begin_time = new Datetime(task.plan_begin_time);

            String s1 = task.plan_interval.substring(0, task.plan_interval.length() - 1);
            String s2 = task.plan_interval.substring(task.plan_interval.length() - 1);

            switch (s2) {
                case "s": //秒
                    last_time.addSecond(Integer.parseInt(s1));
                    break;
                case "m": //分
                    last_time.setSecond(begin_time.getSeconds());
                    last_time.addMinute(Integer.parseInt(s1));
                    break;
                case "h": //时
                    last_time.setMinute(begin_time.getMinutes());
                    last_time.setSecond(begin_time.getSeconds());

                    last_time.addHour(Integer.parseInt(s1));
                    break;
                case "d": //日
                    task._is_day_task = true;
                    last_time.setHour(begin_time.getHours());
                    last_time.setMinute(begin_time.getMinutes());
                    last_time.setSecond(begin_time.getSeconds());

                    last_time.addDay(Integer.parseInt(s1));
                    break;
                case "M": //月
                    task._is_day_task = true;
                    last_time.setHour(begin_time.getHours());
                    last_time.setMinute(begin_time.getMinutes());
                    last_time.setSecond(begin_time.getSeconds());

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


        //2.2.执行
        long _times = do_runTask(task, timecount);

        //2.3.记录性能
        String _node = WaterAdapter.global().localHost();

        WaterClient.Track.track("waterplan", task.tag, task.path, _times, _node);
    }

    private long do_runTask(PaasFileModel task, Timecount timecount) throws Exception {
        //开始执行::
        task.plan_last_time = new Date();
        DbWaterPaasApi.setPlanState(task, 2, "processing");

        //2.执行
        JtRun.exec(task);

        //3.更新状态
        task.plan_count = (task.plan_count + 1) % 10000;
        DbWaterPaasApi.setPlanState(task, 9, "OK");

        long _times = timecount.stop().milliseconds();

        LogUtil.planInfo(this, task, _times);

        return _times;
    }
}
