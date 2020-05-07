package waterapp.dso.db;

import org.noear.weed.DbContext;
import waterapp.utils.TextUtils;
import waterapp.Config;
import waterapp.dso.IDUtil;
import waterapp.models.LoggerModel;
import waterapp.utils.Datetime;
import waterapp.utils.EncryptUtil;

import java.sql.SQLException;

/**
 * 日志服务接口
 * */
public final class DbWaterLogApi {
    private static DbContext db() {
        return Config.water;
    }


    public static DbContext loggerSource(String logger) throws SQLException {
        LoggerModel cfg = DbWaterCfgApi.getLogger(logger);

        return DbWaterCfgApi.getDbContext(cfg.source, Config.water_log);
    }

    //添加日志
//    public static void addLog(String logger, String tag, String tag1, String tag2, String label, String content) throws SQLException {
//
//        DbContext db = loggerSource(logger);
//
//        db.table(logger).usingExpr(true)
//                .set("log_id", IDUtil.buildLogID())
//                .set("tag", tag)
//                .set("tag1", tag1)
//                .set("tag2", tag2)
//                .set("label", label)
//                .set("content", content)
//                .set("log_date", "$DATE(NOW())")
//                .set("log_fulltime", "$NOW()")
//                .insert();
//    }

    public static void addTrack(String service, String schema, long interval, String cmd_sql, String cmd_arg, String operator, String operator_ip, String path, String ua, String note) throws SQLException {

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

        String cmd_sql_md5 = EncryptUtil.md5(cmd_sql);


        if (TextUtils.isEmpty(path)) {
            do_addTrack("water_exam_log_sql", service, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);
        } else {
            do_addTrack("water_exam_log_bcf", service, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);

            if (interval >= 2000) {
                do_addTrack("water_exam_log_sql", service, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);
            }
        }
    }

    private static void do_addTrack(String logger, String service, String schema, String method, int seconds, long interval, String cmd_sql, String cmd_sql_md5, String cmd_arg, String operator, String operator_ip, String path, String ua, String note) throws SQLException {
        DbContext db = Config.water_log;

        Datetime now = Datetime.Now();


        db.table(logger).usingExpr(true)
                .set("log_id", IDUtil.buildLogID())
                .set("`service`", service)
                .set("`schema`", schema)
                .set("`method`", method)
                .set("`cmd_sql`", cmd_sql)
                .set("`cmd_sql_md5`", cmd_sql_md5)
                .set("`cmd_arg`", cmd_arg)
                .set("`seconds`", seconds)
                .set("`interval`", interval)
                .set("`operator`", operator)
                .set("`operator_ip`", operator_ip)
                .set("`path`", path)
                .set("`ua`", ua)
                .set("`note`", note)
                .set("log_date", now.getDate())
                .set("log_hour", now.getHours())
                .set("log_fulltime", "$NOW()")
                .insert();
    }
}