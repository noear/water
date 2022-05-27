package watersev.controller;

import luffy.JtRun;
import org.noear.snack.ONode;
import org.noear.snack.core.Options;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.ContextEmpty;
import org.noear.solon.core.handle.ContextUtil;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.LockUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import watersev.dso.*;
import watersev.dso.db.DbWaterApi;
import watersev.models.water.MonitorModel;
import watersev.utils.CallUtil;

import java.util.List;

/**
 * 数据监视任务（可集群，可多实例运行。会分散监视任务）
 *
 * @author noear
 * */
@Component
public final class MotController implements IJob {
    static Options json_cfg = new Options();

    @Override
    public String getName() {
        return "mot";
    }

    @Override
    public int getInterval() {
        return 1000 * 60; //实际是：60s 跑一次
    }

    @Override
    public void exec() throws Exception {
        RegController.addService("watersev-" + getName());


        //尝试获取锁（60秒内只能调度一次）；避免集群切换时，多次运行
        //
        List<MonitorModel> list = DbWaterApi.getMonitorList();

        for (MonitorModel task : list) {
            if (TextUtils.isEmpty(task.source_query)) {
                continue;
            }

            exec0(task);
        }
    }

    private void exec0(MonitorModel task) {
        CallUtil.asynCall(() -> {
            execDo(task);
        });
    }

    /**
     * 会对每个监视任务进行分布式锁控制，故可以多实例集群部署
     *
     * @param task 监视任务
     */
    private void execDo(MonitorModel task) {
        String threadName = "mot-" + task.monitor_id;
        Thread.currentThread().setName(threadName);

        if (LockUtils.tryLock(WW.watersev_mot, threadName, 59) == false) {
            //尝试获取锁（59秒内只能调度一次），避免集群，多次运行
            return;
        }

        try {
            ContextUtil.currentSet(new ContextEmpty());

            runTask(task);
        } catch (Throwable ex) {
            LogUtil.error(this.getName(), task.monitor_id + "", task.name + "::\n\n" + Utils.throwableToString(ex));
        } finally {
            ContextUtil.currentRemove();
        }
    }

    private void runTask(MonitorModel task) throws Exception {
        if (TextUtils.isEmpty(task.source_query) || task.source_query.indexOf("::") < 0) {
            return;
        }

        String[] tmp = task.source_query.split("::");

        String source = tmp[0].replace("--", "").trim();
        String query = tmp[1].trim();

        MotResult motResult = null;

        if (source.equals("faas()")) {
            motResult = getDataByFaas(source, query, task);
        } else {
            motResult = getDataByDb(source, query, task);
        }

        if (motResult.succeed == false) {
            return;
        }


        ONode model = new ONode().asObject();
        model.set("d", motResult.data);
        model.set("tag", task.task_tag);


        String model_json = model.options(json_cfg).toJson();//date -> "new Date('" + new Datetime(date).toString("yyyy-MM-dd HH:mm:ss") + "')");

        String task_tag = null;

        if (motResult.data.count() > 0) {
            task_tag = RuleUtil.format(model_json, "fmt_tag_" + task.monitor_id, task.task_tag_exp);
        }

        task.alarm_exp = RuleUtil.format(model_json, "fmt_" + task.monitor_id, task.alarm_exp);

        boolean isMatch = RuleUtil.match(model_json, "rule_" + task.monitor_id, task.rule);

        if (task_tag == null) {
            task_tag = "";
        }

        if (isMatch) {
            LogUtil.warn(this.getName(), task.monitor_id + "", task.name + ", match[error]==" + task.alarm_exp + "#" + task_tag + "(alarm)\n" + model_json);
        } else {
            LogUtil.info(this.getName(), task.monitor_id + "", task.name + ", no match[ok]==" + task.alarm_exp + "#" + task_tag + "\n" + model_json);
        }

        if (isMatch) {
            DbWaterApi.setMonitorState(task.monitor_id, task.alarm_count + 1, task_tag);//记录次数

            if (task_tag.startsWith("!")) {
                //!xxx 表示，一直报警
                AlarmUtil.tryAlarm(task, false);//报警
            } else {
                if (task_tag.equals(task.task_tag) == false) {
                    AlarmUtil.tryAlarm(task, false);//报警
                }
            }
        } else {
            if (task.alarm_count > 0) {
                DbWaterApi.setMonitorState(task.monitor_id, 0, task_tag);//记录次数
                AlarmUtil.tryAlarm(task, true);//通知恢复
            }
        }
    }

    /**
     * 基于 FaaS 运行
     */
    private MotResult getDataByFaas(String source, String query, MonitorModel task) throws Exception {
        Object tmp = JtRun.exec(query);

        return MotResult.succeed(tmp);
    }

    /**
     * 基于 Db Sql 运行
     */
    private MotResult getDataByDb(String source, String query, MonitorModel task) throws Exception {
        ConfigM cfg = WaterClient.Config.getByTagKey(source);

        DbContext db = cfg.getDb();

        String sql = SqlUtil.preProcess(query);

        if (SqlUtil.isSafe(sql) == false) {
            LogUtil.warn(this.getName(), task.monitor_id + "", task.name + " is unsafe code::" + sql);
            return MotResult.failure();
        }

        List list = db.sql(sql).getMapList();

        return MotResult.succeed(list);
    }
}
