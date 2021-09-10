package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.Timecount;
import luffy.JtRun;
import watersev.dso.*;
import watersev.dso.db.DbWaterPaasApi;
import watersev.models.water_paas.PaasFileModel;
import watersev.utils.CallUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 定时任务
 * <p>
 * 状态：(成功:9, 出错:8, 处理中:2, 待处理:1（立即执行）, 待处理:0）
 *
 * @author noear
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


    @Override
    public void exec() throws Exception {
        JtRun.initAwait();

        //尝试获取锁（3秒内只能调度一次），避免集群切换时，多次运行
        //
        if (LockUtils.tryLock("waterpln", "waterpln_lock", 3)) {
            try {
                exec0();
            } finally {
                LockUtils.unLock("waterpln", "waterpln_lock");
            }
        }
    }

    private void exec0() throws SQLException {
        List<PaasFileModel> list = DbWaterPaasApi.getPlanList();

        System.out.println("查到任务数：" + list.size());

        for (PaasFileModel task : list) {
            CallUtil.asynCall(() -> {
                doExec(task);
            });
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

            //ex.printStackTrace();

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

        //1.4.检查时间
        Date baseTime = task.plan_last_time;
        if (baseTime == null) {
            baseTime = task.plan_begin_time;
        }

        if (baseTime == null) {
            return;
        }

        //1.5.检查执行时间是否到了
        PlnNext plnNext = null;
        if (task.plan_interval.length() > 7 && task.plan_interval.contains(" ")) {
            //说明是： cron
            plnNext = PlnHelper.getNextTimeByCron(task, baseTime);
        } else {
            //说明是：1s,1m,1h,1d
            plnNext = PlnHelper.getNextTimeBySimple(task, baseTime);
        }

        //1.5.1.如果未到执行时间则返回
        if (plnNext.allow == false) {
            return;
        }

        //1.5.2.如果时间未到则返回
        if (System.currentTimeMillis() < plnNext.datetime.getTime()) {
            return;
        }

        //////////////////////////////////////////


        //2.2.执行
        long _times = runTaskDo(task, timecount);

        //2.3.记录性能
        WaterClient.Track.track("waterplan", task.tag, task.path, _times);
        WaterClient.Track.trackNode("waterplan", WaterClient.localHost(), _times);
    }


    private long runTaskDo(PaasFileModel task, Timecount timecount) throws Exception {
        //开始执行::
        task.plan_last_time = new Date();
        DbWaterPaasApi.setPlanState(task, 2, "processing");

        //2.执行
        JtRun.exec(task);

        //3.更新状态
        task.plan_count = (task.plan_count + 1) % 10000;
        task.plan_last_timespan = timecount.stop().milliseconds();

        if (task.plan_interval.length() > 7 && task.plan_interval.contains(" ")) {
            PlnNext plnNext = PlnHelper.getNextTimeByCron(task, task.plan_last_time);
            task.plan_next_time = plnNext.datetime;
        } else {
            PlnNext plnNext = PlnHelper.getNextTimeBySimple(task, task.plan_last_time);
            task.plan_next_time = plnNext.datetime;
        }

        DbWaterPaasApi.setPlanState(task, 9, "OK");

        LogUtil.planInfo(this, task, task.plan_last_timespan);

        return task.plan_last_timespan;
    }
}
