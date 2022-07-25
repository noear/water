package watersev.dso;

import org.noear.solon.extend.schedule.IJob;
import org.noear.luffy.model.AFileModel;
import org.noear.water.WW;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.utils.HttpResultException;
import org.noear.water.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogUtil {
    private static Logger log_msg = LoggerFactory.getLogger(WW.logger_water_log_msg);
    private static Logger log_sev = LoggerFactory.getLogger(WW.logger_water_log_sev);
    private static Logger log_faas = LoggerFactory.getLogger(WW.logger_water_log_faas);

    public static void writeForMsg(MessageModel msg, String broker, DistributionModel dist, String content) {
        if (dist == null) {
            dist = new DistributionModel();
        }

        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content)
                .append("@").append(dist._duration).append("ms");

        String summary = sb.toString();


        MDC.put("tag0", msg.topic_name);
        MDC.put("tag1", String.valueOf(msg.msg_id));
        MDC.put("tag3", broker);

        if (TextUtils.isEmpty(dist.receive_url)) {
            log_msg.info("{}\r\n{}", summary, content);
        } else {
            log_msg.info("{}\r\n{}", summary, dist.receive_url + "::\r\n" + content);
        }
    }


    public static void writeForMsgByError(MessageModel msg, String broker, DistributionModel dist, String content) {
        if (dist == null) {
            dist = new DistributionModel();
        }

        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content)
                .append("@").append(dist._duration).append("ms");

        String summary = sb.toString();

        MDC.put("tag0", msg.topic_name);
        MDC.put("tag1", String.valueOf(msg.msg_id));
        MDC.put("tag3", broker);

        if (TextUtils.isEmpty(dist.receive_url)) {
            log_msg.error("{}\r\n{}", summary, content);
        } else {
            log_msg.error("{}\r\n{}", summary, dist.receive_url + "::\r\n" + content);
        }

    }

    public static void writeForMsgByError(MessageModel msg, String broker, Throwable ex) {
        StringBuilder sb = new StringBuilder();

        sb.append(msg.msg_id)
                .append("#").append(msg.dist_count)
                .append("#").append(msg.topic_name).append("=").append(msg.content);


        MDC.put("tag0", msg.topic_name);
        MDC.put("tag1", String.valueOf(msg.msg_id));
        MDC.put("tag3", broker);

        log_msg.error("{}\r\n{}", sb, ex);
    }

    //==========================================================
    //
    //
    public static void planInfo(IJob tag, AFileModel plan, long _times) {
        StringBuilder sb = new StringBuilder();
        sb.append(plan.path)
                .append("(").append(plan.plan_count).append("/").append(plan.plan_max)
                .append(")执行成功 - ").append(_times).append("ms");

        MDC.put("tag0", "_plan");
        MDC.put("tag1", plan.tag);
        MDC.put("tag2", plan.path);

        log_faas.info(sb.toString());
    }

    public static void planError(IJob tag, AFileModel plan, long _times, Throwable content) {
        StringBuilder sb = new StringBuilder();
        sb.append(plan.path)
                .append("(").append(plan.plan_count).append("/").append(plan.plan_max)
                .append(")执行失败 - ").append(_times).append("ms");

        MDC.put("tag0", "_plan");
        MDC.put("tag1", plan.tag);
        MDC.put("tag2", plan.path);

        if (content instanceof HttpResultException) {
            log_faas.error("{}\r\n{}", sb, content.getMessage());
        } else {
            log_faas.error("{}\r\n{}", sb, content);
        }
    }

    public static void sevWarn(String tag, String tag1, String content) {
        MDC.put("tag0", tag);
        MDC.put("tag1", tag1);

        log_sev.warn(content);
    }

    public static void sevError(String tag, String tag1, String content) {
        MDC.put("tag0", tag);
        MDC.put("tag1", tag1);

        log_sev.error(content);
    }

    //==========================================================
    //
    //
    public static void info(String tag, String tag1, String content) {
        MDC.put("tag0", tag);
        MDC.put("tag1", tag1);

        log_sev.info(content);
    }

    public static void warn(String tag, String tag1, String content) {
        MDC.put("tag0", tag);
        MDC.put("tag1", tag1);

        log_sev.warn(content);
    }

    public static void error(String tag, String tag1, Throwable content) {
        MDC.put("tag0", tag);
        MDC.put("tag1", tag1);

        log_sev.error("{}", content);
    }

    public static void error(String tag, String tag1, String content) {
        MDC.put("tag0", tag);
        MDC.put("tag1", tag1);

        log_sev.error(content);
    }
}
