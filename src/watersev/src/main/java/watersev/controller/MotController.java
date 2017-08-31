package watersev.controller;

import noear.snacks.ONode;
import noear.weed.DataItem;
import noear.weed.DataList;
import noear.weed.DbContext;
import org.apache.http.util.TextUtils;
import watersev.dao.AlarmUtil;
import watersev.dao.LogUtil;
import watersev.dao.RuleUtil;
import watersev.dao.SqlUtil;
import watersev.dao.db.DbApi;
import watersev.models.water.MonitorModel;
import watersev.models.water.ConfigModel;
import watersev.utils.DateUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MotController {
    public static void main() {
        int hours = DateUtil.getHour(new Date());

        if (hours > 22 || hours < 6) { //半夜不做事
            return;
        }

        List<MonitorModel> list = DbApi.getMonitorList();

        for (MonitorModel task : list) {
            if (TextUtils.isEmpty(task.source_model)) {
                continue;
            }

            try {
                exec(task);
            } catch (Exception ex) {
                ex.printStackTrace();
                LogUtil.doWrite("monitor.error", task.name, ex.getMessage());
                LogUtil.error("monitor.error", task.name, ex);
            }
        }
    }

    private static void exec(MonitorModel task) throws SQLException {
        String[] ss = task.source.split("\\.");
        ConfigModel cfg = DbApi.getConfig(ss[0], ss[1]);

        DbContext db = cfg.getDb();

        String sql = SqlUtil.preProcess(task.source_model);

        if(SqlUtil.isSafe(sql)==false){
            return;
        }

        DataList list = db.sql(sql).getDataList();
        ONode model = new ONode().asArray();
        for (DataItem item : list.getRows()) {
            ONode d = new ONode();
            for (String key : item.keys()) {
                Object val = item.get(key);

                if (val instanceof String) {
                    d.set(key, (String) val);
                } else if (val instanceof Date) {
                    d.set(key, (Date) val);
                } else if (val instanceof Boolean) {
                    d.set(key, (Boolean) val);
                } else if (val instanceof Double) {
                    d.set(key, (Double) val);
                } else if (val instanceof Float) {
                    d.set(key, (Float) val);
                } else {
                    d.set(key, item.getLong2(key));
                }
            }

            model.add(d);
        }

        String model_json = model.toJson(date-> "new Date('"+ DateUtil.format(date,"yyyy-MM-dd HH:mm:ss")+"')");

        String task_tag = RuleUtil.format(model_json, "fmt_tag_" + task.monitor_id, task.task_tag_exp);
        task.alarm_exp = RuleUtil.format(model_json, "fmt_" + task.monitor_id, task.alarm_exp);

        boolean isMatch = RuleUtil.match(model_json, "rule_" + task.monitor_id, task.rule);

        if(task_tag==null){
            task_tag="";
        }

        LogUtil.doWrite("monitor.ok", task.name, (isMatch ? "match[is error]" : "OK")+"=="+task.alarm_exp+"<br>"+model_json);


        if(isMatch) {
            DbApi.setMonitorState(task.monitor_id, task.alarm_count + 1, task_tag);//记录次数
            if(task_tag.equals(task.task_tag) == false) {
                AlarmUtil.tryAlarm(task, false);//报警
            }
        }else{
            if(task.alarm_count>0){
                DbApi.setMonitorState(task.monitor_id, 0, task_tag);//记录次数
                AlarmUtil.tryAlarm(task, true);//通知恢复
            }
        }


    }
}
