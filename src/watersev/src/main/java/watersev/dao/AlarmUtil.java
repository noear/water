package watersev.dao;

import org.apache.http.util.TextUtils;
import watersev.Config;
import watersev.dao.db.DbApi;
import watersev.models.water.MonitorModel;
import watersev.models.water.ServiceModel;
import watersev.models.water_msg.MessageModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuety on 2017/7/27.
 */
public class AlarmUtil {
    public static void tryAlarm(MessageModel msg, boolean isOk, String alarm_mobile) {
        try {
            StringBuilder sb = new StringBuilder();
            if(isOk){
                sb.append("恢复：消息=").append(msg.topic_name).append("，").append("#")
                        .append(msg.msg_id);
            }else {
                sb.append("报警：消息=").append(msg.topic_name).append("，").append("#")
                        .append(msg.msg_id).append("已派发").append(msg.dist_count).append("次失败");
            }

            if(TextUtils.isEmpty(Config.environment) == false){
                sb.append(" --[").append(Config.environment).append("]");
            }

            List<String> alias = buildAlias(alarm_mobile);
            HeiheiApi.push(alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil",ex);
        }
    }

    public static void tryAlarm(ServiceModel sev, boolean isOk,  int code) {
        try {
            StringBuilder sb = new StringBuilder();

            if(isOk){
                sb.append("恢复：服务=").append(sev.name).append("@")
                        .append(sev.address).append(sev.note).append("，状态:").append(code);
            }else{
                sb.append("报警：服务=").append(sev.name).append("@")
                        .append(sev.address).append(sev.note).append("，状态:").append(code);
            }

            if(TextUtils.isEmpty(Config.environment) == false){
                sb.append(" --[").append(Config.environment).append("]");
            }

            List<String> alias = buildAlias(sev.alarm_mobile);
            HeiheiApi.push(alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil",ex);
        }
    }

    public static void tryAlarm(MonitorModel task, boolean isOk) {
        try {
            StringBuilder sb = new StringBuilder();

            if (isOk) {
                if(task.type!=1) { //1=报喜, 不需要恢复
                    sb.append("恢复：监视对象=").append(task.monitor_id).append("#").append(task.name);
                }
            } else {
                String label = (task.type==1?"报喜":"预警");

                sb.append(label);

                if (TextUtils.isEmpty(task.alarm_exp)) {
                    sb.append("：监视对象=").append(task.monitor_id).append("#").append(task.name);
                } else {
                    sb.append("：监视对象=").append(task.monitor_id).append("#").append(task.alarm_exp);
                }
            }

            if(sb.length()==0){
                return;
            }

            if(TextUtils.isEmpty(Config.environment) == false){
                sb.append("    [by ").append(Config.environment).append("]");
            }

            List<String> alias = buildAlias(task.alarm_mobile);
            HeiheiApi.push(alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil", ex);
        }
    }

    private static List<String> buildAlias(String alarm_mobile) throws SQLException{
        List<String> alias = new ArrayList<>();

        List<String> mobiles = DbApi.getAlarmMobileList();

        if (TextUtils.isEmpty(alarm_mobile) == false) {
            for(String m : alarm_mobile.split(",")){
                if(TextUtils.isEmpty(m)==false && alias.contains(m)==false) {
                    alias.add(m);
                }
            }
        }

        for (String m : mobiles) {
            if(TextUtils.isEmpty(m)==false && alias.contains(m)==false) {
                alias.add(m);
            }
        }

        return alias;
    }
}
