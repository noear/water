package waterapp.dso.db;

import org.noear.weed.DbContext;
import org.noear.water.utils.TextUtils;
import waterapp.Config;
import waterapp.dso.CacheUtil;
import waterapp.models.water_log.LogSqlModel;

import java.sql.SQLException;
import java.util.List;

public class DbWaterLogApi {
    private static DbContext db() {
        return Config.water_log;
    }

    public static List<LogSqlModel> getSqlLogsByPage(String tableName, String service, String schema, String method, int seconds, String operator, String path, int log_date, int log_hour, int page, int pageSize) throws SQLException {

        int start = (page - 1) * pageSize;

        return db().table(tableName)
                .where("1 = 1")
                .build(tb -> {
                    if (!TextUtils.isEmpty(service)) {
                        tb.and("`service` = ?", service);
                    }
                    if (!TextUtils.isEmpty(schema)) {
                        tb.and("`schema` = ?", schema);
                    }
                    if (!TextUtils.isEmpty(method)) {
                        tb.and("`method` = ?", method);
                    }

                    if (!TextUtils.isEmpty(operator)) {
                        tb.and("`operator` = ?", operator);
                    }

                    if (!TextUtils.isEmpty(path)) {
                        tb.and("`path` = ?", path);
                    }

                    if (seconds > 0) {
                        tb.and("`seconds` >= ?", seconds);
                    }

                    if (log_date > 0) {
                        tb.and("log_date = ?", log_date);
                    }

                    if (log_hour > 0) {
                        tb.and("log_hour = ?", log_hour);
                    }
                })
                .orderBy("log_id desc")
                .limit(start, pageSize)
                .select("*")
                .getList(new LogSqlModel());
    }


    public static long getSqlLogsCount(String tableName, String service, String schema, String method, int seconds, String operator, String path, int log_date, int log_hour) throws SQLException {
        return db().table(tableName)
                .where("1 = 1")
                .build(tb -> {
                    if (!TextUtils.isEmpty(service)) {
                        tb.and("`service` = ?", service);
                    }
                    if (!TextUtils.isEmpty(schema)) {
                        tb.and("`schema` = ?", schema);
                    }
                    if (!TextUtils.isEmpty(method)) {
                        tb.and("`method` = ?", method);
                    }

                    if (!TextUtils.isEmpty(operator)) {
                        tb.and("`operator` = ?", operator);
                    }

                    if (!TextUtils.isEmpty(path)) {
                        tb.and("`path` = ?", path);
                    }

                    if (seconds > 0) {
                        tb.and("`seconds` >= ?", seconds);
                    }

                    if (log_date > 0) {
                        tb.and("log_date = ?", log_date);
                    }

                    if (log_hour > 0) {
                        tb.and("log_hour = ?", log_hour);
                    }
                })
                .caching(CacheUtil.data).usingCache(60)
                .count();
    }

    public static List<LogSqlModel> getSqlServiceTags(String tableName) throws SQLException {
        return db().table(tableName)
                .where("1 = 1")
                .groupBy("`service`")
                .orderBy("`service` ASC")
                .select("`service` tag")
                .caching(CacheUtil.data).usingCache(60 * 3)
                .getList(new LogSqlModel());
    }

    public static List<LogSqlModel> getSqlSecondsTags(String tableName, String service) throws SQLException {
        return db().table(tableName)
                .where("`service`=?", service)
                .groupBy("seconds")
                .orderBy("seconds ASC")
                .select("seconds tag")
                .caching(CacheUtil.data).usingCache(60 * 3)
                .getList(new LogSqlModel());
    }

    public static List<LogSqlModel> getSqlOperatorTags(String tableName, String service) throws SQLException {
        return db().table(tableName)
                .where("`service`=?", service)
                .groupBy("operator")
                .orderBy("operator ASC")
                .select("operator tag")
                .caching(CacheUtil.data).usingCache(60 * 3)
                .getList(new LogSqlModel());
    }
}
