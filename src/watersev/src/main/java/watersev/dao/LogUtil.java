package watersev.dao;

import org.apache.commons.lang.exception.ExceptionUtils;
import watersev.models.water_msg.MessageModel;

/**
 * Created by yuety on 2017/7/18.
 */
public class LogUtil {
    public static void writeForMsg(String tag, MessageModel msg, String content) {
        String label = msg.msg_id + "#" + msg.dist_count + "#" + msg.topic_name + "=" + msg.content;

        doWrite(tag, 0, label, content);
    }

    public static void doWrite(String tag, String label, String content) {
        doWrite(tag, 0, label, content);
    }

    public static void doWrite(String tag, long tag1, String label, String content) {
        WaterApi.Logger.append("water_log_sev", 0, tag, tag1, label, content);

        System.out.print(tag + "::\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }


    public static void writeForMsgByError(String tag, MessageModel msg, String content) {
        String label = msg.msg_id + "#" + msg.dist_count + "#" + msg.topic_name + "=" + msg.content;

        WaterApi.Logger.append("water_log_sev_error", 0, tag, 0,label, content);

        System.out.print(tag + "::\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

    public static void error(String tag, Exception ex) {
        error(tag, "", ex);
    }

    public static void error(String tag,String label , Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\r\n").append(ExceptionUtils.getFullStackTrace(ex));

        WaterApi.Logger.append("water_log_sev_error", 0, tag, 0,label, sb.toString());

        System.out.print(tag + "::\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }
}
