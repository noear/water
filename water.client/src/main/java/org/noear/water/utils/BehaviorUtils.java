package org.noear.water.utils;

import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.model.LogM;
import org.noear.weed.Command;

/**
 * @author noear 2022/6/30 created
 */
public class BehaviorUtils {

    /**
     * 跟踪SQL性能
     *
     * @param service        服务名
     * @param thresholdValue 阈值
     */
    public static void trackOfPerformance(String service, Command cmd, long thresholdValue) {
        long timespan = cmd.timespan();

        if (timespan > thresholdValue) {
            track0(service, cmd, null, null, null, WaterClient.localHost());
        }
    }

    /**
     * 跟踪SQL行为
     *
     * @param service     服务名
     * @param ua          ua
     * @param path        请求路径
     * @param operator    操作人
     * @param operator_ip 操作IP
     */
    public static void trackOfBehavior(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        track0(service, cmd, ua, path, operator, operator_ip);
    }

    /**
     * 跟踪SQL命令性能
     */
    private static void track0(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        long interval = cmd.timespan();
        String trace_id = WaterClient.waterTraceId();

        track0Do(service, trace_id, cmd, interval, ua, path, operator, operator_ip);
    }

    private static void track0Do(String service, String trace_id, Command cmd, long interval, String ua, String path, String operator, String operator_ip) {
        int seconds = (int) (interval / 1000);
        String schema = cmd.context.schema();

        String sqlUp = cmd.text.toUpperCase();

        String method = "OTHER";
        if (sqlUp.indexOf("SELECT ") >= 0) {
            method = "SELECT";
        } else if (sqlUp.indexOf("UPDATE ") >= 0) {
            method = "UPDATE";
        } else if (sqlUp.indexOf("DELETE ") >= 0) {
            method = "DELETE";
        } else if (sqlUp.indexOf("INSERT INTO ") >= 0) {
            method = "INSERT";
        }

        LogM logM = new LogM();

        if (path == null) {
            logM.logger = WW.logger_water_log_sql_p;
        } else {
            logM.logger = WW.logger_water_log_sql_b;
        }

        logM.trace_id = trace_id;

        logM.level = 1;

        logM.tag = String.valueOf(seconds);//留空
        logM.tag1 = path; //秒数
        logM.tag2 = operator;
        logM.tag3 = method;
        logM.tag4 = "";

        logM.weight = interval; //=tag5, 毫秒数

        logM.group = schema; //=tag6
        logM.service = service;//=tag7

        StringBuilder content = new StringBuilder();

        content.append(schema).append("::").append(cmd.text);
        content.append("<n-l>$$$").append(ONode.stringify(cmd.paramMap())).append("</n-l>");

        logM.content = content.toString();
        logM.from = operator_ip;

        WaterClient.Log.append(logM);
    }
}
