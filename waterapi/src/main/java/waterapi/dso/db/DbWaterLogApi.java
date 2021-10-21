package waterapi.dso.db;

import org.noear.water.protocol.utils.SnowflakeUtils;
import org.noear.water.utils.*;
import waterapi.Config;
import waterapi.dso.TrackBcfPipelineLocal;
import waterapi.dso.TrackSqlPipelineLocal;
import waterapi.models.TrackEvent;

import java.sql.SQLException;
import java.util.List;

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
            do_addTrack(TrackSqlPipelineLocal.singleton(), "water_exam_log_sql", service, trace_id, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);
        } else {
            do_addTrack(TrackBcfPipelineLocal.singleton(), "water_exam_log_bcf", service, trace_id, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);

            if (interval >= 2000) {
                do_addTrack(TrackSqlPipelineLocal.singleton(), "water_exam_log_sql", service, trace_id, schema, method, seconds, interval, cmd_sql, cmd_sql_md5, cmd_arg, operator, operator_ip, path, ua, note);
            }
        }
    }

    private static void do_addTrack(EventPipeline<TrackEvent> pipeline, String logger, String service, String trace_id, String schema, String method, int seconds, long interval, String cmd_sql, String cmd_sql_md5, String cmd_arg, String operator, String operator_ip, String path, String ua, String note) throws SQLException {
        TrackEvent event = new TrackEvent();

        Datetime now = Datetime.Now();

        event.log_id = SnowflakeUtils.genId(); //ProtocolHub.idBuilder.getLogId(logger);
        event.service = service;
        event.trace_id = trace_id;
        event.schema = schema;
        event.method = method;
        event.cmd_sql = cmd_sql;
        event.cmd_sql_md5 = cmd_sql_md5;
        event.cmd_arg = cmd_arg;
        event.seconds = seconds;
        event.interval = interval;
        event.operator = operator;
        event.operator_ip = operator_ip;
        event.path = path;
        event.ua = ua;
        event.note = note;
        event.log_date = now.getDate();
        event.log_hour = now.getHours();
        event.log_fulltime = now.getFulltime().getTime();

        pipeline.add(event);
    }

    public static void addTrackAll(String logger, List<TrackEvent> eventList) throws SQLException {
        Config.water_log.table(logger).usingExpr(false).usingNull(true)
                .insertList(eventList, (d, m) -> {
                    if(d.cmd_arg != null && d.cmd_arg.length() > 8000) {
                        d.cmd_arg = d.cmd_arg.substring(0, 8000) + "...";
                    }

                    m.set("log_id", d.log_id)
                            .set("service", d.service)
                            .set("trace_id", d.trace_id)
                            .set("schema", d.schema)
                            .set("method", d.method)
                            .set("cmd_sql", d.cmd_sql)
                            .set("cmd_sql_md5", d.cmd_sql_md5)
                            .set("cmd_arg", d.cmd_arg)
                            .set("seconds", d.seconds)
                            .set("interval", d.interval)
                            .set("operator", d.operator)
                            .set("operator_ip", d.operator_ip)
                            .set("path", d.path)
                            .set("ua", d.ua)
                            .set("note", d.note)
                            .set("log_date", d.log_date)
                            .set("log_hour", d.log_hour)
                            .set("log_fulltime", d.log_fulltime);
                });

    }
}