package watersev.dso;

import org.noear.solon.extend.schedule.IJob;
import org.noear.luffy.model.AFileModel;
import org.noear.water.log.Logger;
import org.noear.water.log.WaterLogger;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.utils.TextUtils;

public class LogUtil {
    private static Logger log_msg =  WaterLogger.get("water_log_msg");
    private static Logger log_sev =  WaterLogger.get("water_log_sev");
    private static Logger log_paas =  WaterLogger.get("water_log_paas");

    public static void writeForMsg(MessageModel msg, DistributionModel dist, String content) {
        if (dist == null) {
            dist = new DistributionModel();
        }

        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content)
                .append("@").append(dist._duration).append("ms");

        String summary =  sb.toString();


        if (TextUtils.isEmpty(dist.receive_url)) {
            log_msg.info(msg.topic_name, msg.msg_id + "", summary, content);
        }
        else {
            log_msg.info(msg.topic_name, msg.msg_id + "", summary, dist.receive_url + "::\r\n" + content);
        }
    }

    public static void writeForMsg(MessageModel msg, String content) {
        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content);

        String summary = sb.toString();

        log_msg.info(msg.topic_name, msg.msg_id + "", summary, content);
    }

    public static void writeForMsgByError(MessageModel msg, DistributionModel dist, String content) {
        if (dist == null) {
            dist = new DistributionModel();
        }

        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content)
                .append("@").append(dist._duration).append("ms");

        String summary =  sb.toString();

        if (TextUtils.isEmpty(dist.receive_url)) {
            log_msg.error(msg.topic_name, msg.msg_id + "", summary, content);
        }
        else {
            log_msg.error(msg.topic_name, msg.msg_id + "", summary, dist.receive_url + "::\r\n" + content);
        }

    }

    public static void writeForMsgByError(MessageModel msg, Throwable ex) {
        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content);

        String summary =  sb.toString();

        log_msg.error(msg.topic_name, msg.msg_id + "", sb.toString(), ex);
    }

    //==========================================================
    //
    //
    public static void planInfo(IJob tag, AFileModel plan, long _times) {
        StringBuilder sb = new StringBuilder();
        sb.append(plan.path)
                .append("(").append(plan.plan_count).append("/").append(plan.plan_max)
                .append(")执行成功 - ").append(_times).append("ms");

        log_paas.info("_plan", plan.tag, plan.path, "", "", sb.toString());
    }

    public static void planError(IJob tag, AFileModel plan, Throwable content) {
        log_paas.error("_plan", plan.tag, plan.path, "", "", content);
    }



    public static void write(IJob tag, String tag1, String summary, String content) {
        write(tag.getName(), tag1, summary, content);
    }

    public static void write(String tag, String summary, String content) {
        write(tag, null, summary, content);
    }

    public static void write(String tag, String tag1, String summary, String content) {
        log_sev.info(tag, tag1, summary, content);
    }

    public static void error(IJob tag, String tag1, String summary, Throwable content) {
        log_sev.error(tag.getName(), tag1, summary, content);
    }

    public static void error(String tag, String tag1, String summary, Throwable content) {
        log_sev.error(tag, tag1, summary, content);
    }

    public static void error(String tag, String tag1, String summary, String content) {
        log_sev.error(tag, tag1, summary, content);
    }

    public static void error(String tag, String tag1, String tag2, String summary, String content) {
        log_sev.error(tag, tag1, tag2, summary, content);
    }

    public static void debug(IJob tag, String summary, String content) {
        log_sev.debug(tag.getName(), summary, content);
    }

    public static void debug(String tag, String summary, String content) {
        log_sev.debug(tag, summary, content);
    }
}
