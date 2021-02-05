package waterapi.dso.db;

import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import waterapi.Config;

import java.sql.SQLException;

/**
 * 日志服务接口
 * */
public final class DbWaterLogApi {


    public static void addTrack(String service, String trace_id, String schema, long interval, String cmd_sql, String cmd_arg, String operator, String operator_ip, String path, String ua, String note) throws SQLException {

        int seconds = (int) (interval / 1000);
        String sqlUp = cmd_sql.toUpperCase();
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

        if (path == null) {
            path = "";
        }

        if (operator == null) {
            operator = "";
        }

        if (operator_ip == null) {
            operator_ip = "";
        }

        String cmd_sql_md5 = EncryptUtils.md5(cmd_sql);


        if (TextUtils.isEmpty(path)) {
            do_addTrack("water_exam_log_sql", service, trace_id, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);
        } else {
            do_addTrack("water_exam_log_bcf", service, trace_id, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);

            if (interval >= 2000) {
                do_addTrack("water_exam_log_sql", service, trace_id, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);
            }
        }
    }

    private static void do_addTrack(String logger, String service, String trace_id, String schema, String method, int seconds, long interval, String cmd_sql, String cmd_sql_md5, String cmd_arg, String operator, String operator_ip, String path, String ua, String note) throws SQLException {
        DbContext db = Config.water_log;

        Datetime now = Datetime.Now();

        long log_id = ProtocolHub.idBuilder.getLogId(logger);

        db.table(logger).usingExpr(true)
                .set("log_id", log_id)
                .set("service", service)
                .set("trace_id", trace_id)
                .set("schema", schema)
                .set("method", method)
                .set("cmd_sql", cmd_sql)
                .set("cmd_sql_md5", cmd_sql_md5)
                .set("cmd_arg", cmd_arg)
                .set("seconds", seconds)
                .set("interval", interval)
                .set("operator", operator)
                .set("operator_ip", operator_ip)
                .set("path", path)
                .set("ua", ua)
                .set("note", note)
                .set("log_date", now.getDate())
                .set("log_hour", now.getHours())
                .set("log_fulltime", "$NOW()")
                .insert();
    }
}