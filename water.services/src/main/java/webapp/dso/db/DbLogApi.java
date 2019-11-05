package webapp.dso.db;

import org.noear.water.tools.Datetime;
import org.noear.water.tools.EncryptUtils;
import org.noear.water.tools.TextUtils;
import org.noear.water.tools.log.Level;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dso.CacheUtil;
import webapp.dso.IDUtil;
import webapp.model.LoggerModel;

import java.sql.SQLException;

public final class DbLogApi {
    private static DbContext db(){
        return Config.water;
    }


    public static DbContext loggerSource(String logger) throws SQLException{

        LoggerModel cfg =
        db().table("water_base_logger").where("logger=?",logger).limit(1)
                .select("*")
                .caching(CacheUtil.data)
                .getItem(LoggerModel.class);


        return DbApi.getDbContext(cfg.source, Config.water_log);
    }

    //添加日志
    public static void addLog(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, String content, String from) throws SQLException {

        DbContext db = loggerSource(logger);

        db.table(logger).usingExpr(true)
                .set("log_id", IDUtil.buildLogID())
                .set("level", level.code)
                .set("tag", tag)
                .set("tag1", tag1)
                .set("tag2", tag2)
                .set("tag3", tag3)
                .set("summary", summary)
                .set("content", content)
                .set("from", from)
                .set("log_date", "$DATE(NOW())")
                .set("log_fulltime", "$NOW()")
                .insert();
    }

    public static void addTrack(String service,  String schema, long interval,String cmd_sql,String cmd_arg, String operator,String operator_ip, String path, String ua, String note) throws SQLException {

        int seconds = (int)(interval/1000);
        String sqlUp = cmd_sql.toUpperCase();
        String method = "OTHER";

        if (sqlUp.indexOf("UPDATE ") >= 0) {
            method = "UPDATE";
        } else if (sqlUp.indexOf("DELETE ") >= 0) {
            method = "DELETE";
        } else if (sqlUp.indexOf("INSERT INTO ") >= 0) {
            method = "INSERT";
        } else if (sqlUp.indexOf("SELECT ") >= 0) {
            method = "SELECT";
        }

        if(path == null){
            path = "";
        }

        if(operator == null){
            operator = "";
        }

        if(operator_ip == null){
            operator_ip = "";
        }

        String cmd_sql_md5 = EncryptUtils.md5(cmd_sql);


        if (TextUtils.isEmpty(path)) {
            do_addTrack("water_log_sql_speed", service, schema, method, seconds,interval, cmd_sql,cmd_sql_md5, cmd_arg, operator,operator_ip, path, ua, note);
        } else {
            do_addTrack("water_log_sql_behavior", service, schema, method,seconds, interval, cmd_sql,cmd_sql_md5, cmd_arg, operator,operator_ip, path, ua, note);

            if (interval >= 2000) {
                do_addTrack("water_log_sql_speed", service, schema, method,seconds, interval, cmd_sql,cmd_sql_md5, cmd_arg, operator,operator_ip, path, ua, note);
            }
        }
    }

    private static void do_addTrack(String logger,String service,  String schema, String method,int seconds, long interval,String cmd_sql,String cmd_sql_md5,String cmd_arg,String operator,String operator_ip, String path, String ua, String note) throws SQLException {
        DbContext db = Config.water_log;

        Datetime now = Datetime.Now();


        db.table(logger).usingExpr(true)
                .set("log_id", IDUtil.buildLogID())
                .set("`service`", service)
                .set("`schema`", schema)
                .set("`method`",method)
                .set("`cmd_sql`",cmd_sql)
                .set("`cmd_sql_md5`",cmd_sql_md5)
                .set("`cmd_arg`",cmd_arg)
                .set("`seconds`", seconds)
                .set("`interval`", interval)
                .set("`operator`", operator)
                .set("`operator_ip`", operator_ip)
                .set("`path`", path)
                .set("`ua`", ua)
                .set("`note`", note)
                .set("log_date", now.getDate())
                .set("log_hour",now.getHours())
                .set("log_fulltime", "$NOW()")
                .insert();
    }
}
