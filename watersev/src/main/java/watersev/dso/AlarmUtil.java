package watersev.dso;

import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.utils.TextUtils;
import watersev.Config;
import watersev.dso.db.DbWaterCfgApi;
import watersev.models.StateTag;
import watersev.models.water.*;
import watersev.models.water_paas.LuffyFileModel;
import watersev.models.water_reg.ServiceModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlarmUtil {
    public static void tryAlarm(StateTag stateTag, boolean isOk, DistributionModel task) {
        try {
            StringBuilder sb = new StringBuilder();

            if (stateTag.topic().alarm_model == 1) {
                return;
            }

            if (isOk) {
                sb.append("恢复：消息=").append(stateTag.msg.topic_name).append("，").append("#")
                        .append(stateTag.msg.msg_id);
            } else {
                if (stateTag.isDistributionEnd()) { //是否已派发结束（超出超大派发次数）
                    sb.append("提示：消息=").append(stateTag.msg.topic_name).append("，").append("#")
                            .append(stateTag.msg.msg_id).append("结束（已派").append(stateTag.msg.dist_count).append("次）");
                } else {
                    sb.append("报警：消息=").append(stateTag.msg.topic_name).append("，").append("#")
                            .append(stateTag.msg.msg_id).append("已派发").append(stateTag.msg.dist_count).append("次失败");
                }
            }

            String tag = stateTag.msg.topic_name.split("\\.|_")[0];

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(tag, task.alarm_mobile);
            ProtocolHub.heihei.push("msg", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil",  "", ex);
        }
    }

    public static void tryAlarm(ServiceModel task, boolean isOk, int code) {
        if (task.is_enabled == false) {
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
                        .append(task.address).append(task.meta).append("，状态:").append(code);
            } else {
                sb.append("报警：服务=").append(task.name).append("@")
                        .append(task.address).append(task.meta).append("，状态:").append(code);
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.tag, task.alarm_mobile);
            ProtocolHub.heihei.push("sev", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil",  "", ex);
        }
    }

    public static void tryAlarm(SynchronousModel task, boolean isOk, long max_id) {
        try {
            StringBuilder sb = new StringBuilder();

            if (isOk) {
                sb.append("提醒：同步成功=").append(task.name).append("@").append(max_id + "");
            } else {
                sb.append("提醒：同步失败=").append(task.name).append("@").append(max_id + "");
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.tag, task.alarm_mobile);
            ProtocolHub.heihei.push("syn", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil",  "", ex);
        }
    }


    public static void tryAlarm(LuffyFileModel task) {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("报警：定时任务异常=").append(task.tag).append("::").append(task.path)
                    .append("@").append("pln").append("@").append(task.file_id);

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.tag, task.alarm_mobile);
            ProtocolHub.heihei.push("pln", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil",  "", ex);
        }
    }


    public static void tryAlarm(MonitorModel task, boolean isOk) {
        try {
            StringBuilder sb = new StringBuilder();

            if (isOk) {
                //if (task.type != 1) { //1=报喜, 不需要恢复
                    sb.append("恢复正常：").append(task.tag).append("::").append(task.name);
                //}
            } else {
                if (TextUtils.isEmpty(task.alarm_exp)) {
                    sb.append(task.name);
                } else {
                    sb.append(task.alarm_exp);
                }
            }

            if (sb.length() == 0) {
                return;
            }

            if (TextUtils.isEmpty(task.alarm_sign)) {
                task.alarm_sign = "数据监视";
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.task_tag, task.alarm_mobile);
            ProtocolHub.heihei.push("mot", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil",  "", ex);
        }
    }

    public static void tryAlarmOnError(MonitorModel task) {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("报警：数据监视异常=").append(task.tag).append("::").append(task.name)
                    .append("@").append("mot").append("@").append(task.monitor_id);

            buildSign(sb, "");

            List<String> alias = buildAlias(task.tag, "");
            ProtocolHub.heihei.push("mot", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil", "", ex);
        }
    }


    public static void tryAlarm(DetectionModel task, boolean isOk, int code) {
        if (task.is_enabled == 0) {
            return;
        }

        if (TextUtils.isEmpty(task.alarm_sign)) {
            task.alarm_sign = "应用监视";
        }

        try {
            StringBuilder sb = new StringBuilder();

            if (isOk) {
                sb.append("恢复：").append(task.tag).append("::").append(task.name).append("@")
                        .append(task.protocol).append("://").append(task.address).append("，状态:").append(code);
            } else {
                sb.append("报警：").append(task.tag).append("::").append(task.name).append("@")
                        .append(task.protocol).append("://").append(task.address).append("，状态:").append(code);
            }

            buildSign(sb, task.alarm_sign);

            List<String> alias = buildAlias(task.tag, task.alarm_mobile);
            ProtocolHub.heihei.push("sev", alias, sb.toString());

        } catch (Exception ex) {
            LogUtil.error("AlarmUtil", "", ex);
        }
    }

    private static void buildSign(StringBuilder sb, String alarm_sign) {
        if (TextUtils.isEmpty(alarm_sign) && TextUtils.isEmpty(Config.alarm_sign())) {
            return;
        }

        sb.append("\n\n");

        if (TextUtils.isEmpty(alarm_sign) == false) {
            sb.append("[").append(alarm_sign).append("]");
        }

        if (TextUtils.isEmpty(Config.alarm_sign()) == false) {
            sb.append("[").append(Config.alarm_sign()).append("]");
        }
    }

    private static List<String> buildAlias(String tag, String alarm_mobile) throws SQLException {
        List<String> alias = new ArrayList<>();


        if (TextUtils.isEmpty(alarm_mobile) == false) {
            for (String m : alarm_mobile.split(",")) {
                if (TextUtils.isEmpty(m) == false && alias.contains(m) == false) {
                    alias.add(m);
                }
            }
        }

        buildAliasByTag("@alarm", alias);
        buildAliasByTag("@" + tag, alias);

        return alias;
    }

    private static void buildAliasByTag(String listTag, List<String> alias) throws SQLException {
        if (listTag.startsWith("@") && listTag.length() > 2) {
            List<String> mobiles = DbWaterCfgApi.getAlarmMobiles(listTag.replace("@", "_"));
            for (String m : mobiles) {
                if (TextUtils.isEmpty(m) == false && alias.contains(m) == false) {
                    alias.add(m);
                }
            }
        }
    }
}
