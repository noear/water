package watersev.controller;

import org.noear.snack.ONode;
import org.noear.snack.core.Constants;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.RuleUtil;
import watersev.dso.SqlUtil;
import watersev.dso.db.DbWaterApi;
import watersev.models.water.MonitorModel;

import java.sql.SQLException;
import java.util.List;

@Component
public final class MotController implements IJob {
    static Constants json_cfg = new Constants();

    @Override
    public String getName() {
        return "mot";
    }

    @Override
    public int getInterval() {
        return 1000 * 60;
    }

    @Override
    public void exec() throws Exception {
        Datetime time = Datetime.Now();
        int hours = time.getHours();

        if (hours > 22 || hours < 6) { //半夜不做事
            return;
        }

        List<MonitorModel> list = DbWaterApi.getMonitorList();

        for (MonitorModel task : list) {
            if (TextUtils.isEmpty(task.source_query)) {
                continue;
            }

            try {
                doExec(task);
            } catch (Exception ex) {
                ex.printStackTrace();
                LogUtil.write(this,task.monitor_id+"", task.name, ex.getMessage());
                LogUtil.error(this, task.monitor_id+"", task.name, ex);
            }
        }
    }

    private  void doExec(MonitorModel task) throws SQLException {
        if(TextUtils.isEmpty(task.source_query) || task.source_query.indexOf("::")<0) {
            return;
        }

        String[] tmp = task.source_query.split("::");

        String source = tmp[0].replace("--","").trim();
        String query  = tmp[1].trim();

        ConfigM cfg = WaterClient.Config.getByTagKey(source);

        DbContext db = cfg.getDb();

        String sql = SqlUtil.preProcess(query);

        if (SqlUtil.isSafe(sql) == false) {
            LogUtil.write(this, task.monitor_id+"", task.name + "（非安全代码）", "-1::"+sql);
            return;
        }

        List list = db.sql(sql).getMapList();

        ONode model = new ONode().asObject();
        model.set("d", ONode.loadObj(list));
        model.set("tag", task.task_tag);


        String model_json = model.cfg(json_cfg).toJson();//date -> "new Date('" + new Datetime(date).toString("yyyy-MM-dd HH:mm:ss") + "')");

        String task_tag = null;

        if(list.size()>0) {
            task_tag = RuleUtil.format(model_json, "fmt_tag_" + task.monitor_id, task.task_tag_exp);
        }

        task.alarm_exp = RuleUtil.format(model_json, "fmt_" + task.monitor_id, task.alarm_exp);

        boolean isMatch = RuleUtil.match(model_json, "rule_" + task.monitor_id, task.rule);

        if (task_tag == null) {
            task_tag = "";
        }

        if (isMatch) {
            LogUtil.write(this, task.monitor_id+"", task.name, "match[error]==" + task.alarm_exp + "#" + task_tag + "\n" + model_json);
        } else {
            LogUtil.write(this, task.monitor_id+"", task.name, "no match[ok]==" + task.alarm_exp + "#" + task_tag + "\n" + model_json);
        }

        if (isMatch) {
            DbWaterApi.setMonitorState(task.monitor_id, task.alarm_count + 1, task_tag);//记录次数
            if (task.rule.indexOf("m.tag") >= 0 || task_tag.equals(task.task_tag) == false) {
                AlarmUtil.tryAlarm(task, false);//报警
            }
        } else {
            if (task.alarm_count > 0) {
                DbWaterApi.setMonitorState(task.monitor_id, 0, task_tag);//记录次数
                AlarmUtil.tryAlarm(task, true);//通知恢复
            }
        }
    }
}
