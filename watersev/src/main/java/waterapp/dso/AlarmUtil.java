package waterapp.dso;

import org.noear.water.WaterClient;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterapp.Config;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.models.water.*;
import waterapp.models.water_msg.DistributionModel;
import waterapp.models.water_msg.MessageModel;
import waterapp.models.water_paas.PaasFileModel;
import waterapp.models.water_reg.ServiceModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmUtil {
    public static void tryAlarm(MessageModel msg, boolean isOk, DistributionModel task) {
        try {
            StringBuilder sb = new StringBuilder();

            if (msg.topic().alarm_model == 1) {
                return;
            }

            if (isOk) {
                sb.append("恢复：消息=").append(msg.topic_name).append("，").append("#")
                        .append(msg.msg_id);
            } else {
                if(msg.isDistributionEnd()) { //是否已派发结束（超出超大派发次数）
                    sb.append("提示：消息=").append(msg.topic_name).append("，").append("#")
                            .append(msg.msg_id).append("结束（已派").append(msg.dist_count).append("次）");
                }else {
                    sb.append("报警：消息=").append(msg.topic_name).append("，").append("#")
                            .append(msg.msg_id).append("已派发").append(msg.dist_count).append("次失败");
                }
            }


            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.alarm_mobile);
            ProtocolHub.heihei.push("msg",alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil",null,"", ex);
        }
    }

    public static void tryAlarm(ServiceModel task, boolean isOk, int code) {
        if(task.is_enabled == false){
            return;
        }

        //非稳定IP的，不需要报警（已由其它框架处理）
        if (task.is_unstable && !isOk && code == 0) {
            //非0表示http状态码；0 表示服务器不存在
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();

            if (isOk) {
                sb.append("恢复：服务=").append(task.name).append("@")
                        .append(task.address).append(task.note).append("，状态:").append(code);
            } else {
                sb.append("报警：服务=").append(task.name).append("@")
                        .append(task.address).append(task.note).append("，状态:").append(code);
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.alarm_mobile);
            ProtocolHub.heihei.push("sev", alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil", null, "", ex);
        }
    }

    public static void tryAlarm(SynchronousModel task, boolean isOk, long max_id) {
        try {
            StringBuilder sb = new StringBuilder();

            if(isOk){
                sb.append("提醒：同步成功=").append(task.name).append("@").append(max_id+"");
            }else{
                sb.append("提醒：同步失败=").append(task.name).append("@").append(max_id+"");
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.alarm_mobile);
            ProtocolHub.heihei.push("syn",alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil",null,"",ex);
        }
    }

    public static void tryNotice(String text, String alarm_mobile) {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("通知：").append(text);

            buildSign(sb, "");

            List<String> alias = buildAlias(alarm_mobile);
            ProtocolHub.heihei.push("alert", alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil", null, "", ex);
        }
    }

    public static void tryAlarm(PaasFileModel task) {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("报警：计划任务出错=").append(task.tag).append("::").append(task.path)
                    .append("@").append("pln").append("@").append(task.file_id);

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.alarm_mobile);
            ProtocolHub.heihei.push("pln",alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil",null,"", ex);
        }
    }


    public static void tryAlarm(MonitorModel task, boolean isOk) {
        try {
            StringBuilder sb = new StringBuilder();

            if (isOk) {
                if(task.type!=1) { //1=报喜, 不需要恢复
                    sb.append("恢复正常：").append(task.name);
                }
            } else {
                String label = (task.type==1?"简报":"预警");

                sb.append(label);

                if (TextUtils.isEmpty(task.alarm_exp)) {
                    sb.append("：").append(task.name);
                } else {
                    sb.append("：").append(task.alarm_exp);
                }
            }

            if(sb.length()==0){
                return;
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.alarm_mobile);
            ProtocolHub.heihei.push("mot",alias, sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error("AlarmUtil",null,"", ex);
        }
    }

    private static void buildSign(StringBuilder sb, String alarm_sign) {
        if (TextUtils.isEmpty(alarm_sign) && TextUtils.isEmpty(Config.alarm_sign())) {
            return;
        }

        sb.append("\r\n\r\n");

        if (TextUtils.isEmpty(alarm_sign) == false) {
            sb.append("[").append(alarm_sign).append("]");
        }

        if (TextUtils.isEmpty(Config.alarm_sign()) == false) {
            sb.append("[").append(Config.alarm_sign()).append("]");
        }
    }

    private static List<String> buildAlias(String alarm_mobile) throws SQLException{
        List<String> alias = new ArrayList<>();

        List<String> mobiles = DbWaterCfgApi.getAlarmMobiles();

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
