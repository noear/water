package webapp.dso.db;

import org.apache.http.util.TextUtils;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dao.CacheUtil;
import webapp.models.water_log.LogModel;
import webapp.models.water_log.LogSqlModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author:Fei.chu
 * @Description:
 */
public class DbWaterLogApi {
    private static DbContext db() {
        return Config.water_log;
    }

    public static List<LogModel> getLogs(String tableName,String tag,String tag1, String tag2 ,int log_date,long log_id,Integer level) throws SQLException{
        return db().table(tableName)
                .where("1 = 1")
                .expre(tb -> {
                    if (!TextUtils.isEmpty(tag)) {
                        tb.and("tag = ?",tag);
                    }
                    if (!TextUtils.isEmpty(tag1)) {
                        tb.and("tag1 = ?",tag1);
                    }
                    if (!TextUtils.isEmpty(tag2)) {
                        tb.and("tag2 = ?",tag2);
                    }
                    if (log_date > 0) {
                        tb.and("log_date = ?",log_date);
                    }
                    if (log_id > 0) {
                        tb.and("log_id <= ?",log_id);
                    }
                    if(level!= null){
                        tb.and("level=?",level);
                    }
                })
                .orderBy("log_id desc")
                .limit(80)
                .select("*")
                .getList(new LogModel());
    }

    public static List<LogSqlModel> getSqlLogsByPage(String tableName,String service,String schema, String method ,int seconds, String operator,String path, int log_date,int log_hour, int page, int pageSize) throws SQLException{

        int start = (page - 1) * pageSize;

        return db().table(tableName)
                .where("1 = 1")
                .expre(tb -> {
                    if (!TextUtils.isEmpty(service)) {
                        tb.and("`service` = ?",service);
                    }
                    if (!TextUtils.isEmpty(schema)) {
                        tb.and("`schema` = ?",schema);
                    }
                    if (!TextUtils.isEmpty(method)) {
                        tb.and("`method` = ?",method);
                    }

                    if (!TextUtils.isEmpty(operator)) {
                        tb.and("`operator` = ?",operator);
                    }

                    if (!TextUtils.isEmpty(path)) {
                        tb.and("`path` = ?",path);
                    }

                    if (seconds>0) {
                        tb.and("`seconds` >= ?",seconds);
                    }

                    if (log_date > 0) {
                        tb.and("log_date = ?",log_date);
                    }

                    if (log_hour > 0) {
                        tb.and("log_hour = ?",log_hour);
                    }
                })
                .orderBy("log_id desc")
                .limit(start, pageSize)
                .select("*")
                .getList(new LogSqlModel());
    }


    public static long getSqlLogsCount(String tableName,String service,String schema, String method ,int seconds, String operator,String path, int log_date, int log_hour) throws SQLException{
        return db().table(tableName)
                .where("1 = 1")
                .expre(tb -> {
                    if (!TextUtils.isEmpty(service)) {
                        tb.and("`service` = ?",service);
                    }
                    if (!TextUtils.isEmpty(schema)) {
                        tb.and("`schema` = ?",schema);
                    }
                    if (!TextUtils.isEmpty(method)) {
                        tb.and("`method` = ?",method);
                    }

                    if (!TextUtils.isEmpty(operator)) {
                        tb.and("`operator` = ?",operator);
                    }

                    if (!TextUtils.isEmpty(path)) {
                        tb.and("`path` = ?",path);
                    }

                    if (seconds>0) {
                        tb.and("`seconds` >= ?",seconds);
                    }

                    if (log_date > 0) {
                        tb.and("log_date = ?",log_date);
                    }

                    if (log_hour > 0) {
                        tb.and("log_hour = ?",log_hour);
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
                .caching(CacheUtil.data).usingCache(60*3)
                .getList(new LogSqlModel());
    }

    public static List<LogSqlModel> getSqlSecondsTags(String tableName, String service) throws SQLException {
        return db().table(tableName)
                .where("`service`=?",service)
                .groupBy("seconds")
                .orderBy("seconds ASC")
                .select("seconds tag")
                .caching(CacheUtil.data).usingCache(60*3)
                .getList(new LogSqlModel());
    }

    public static List<LogSqlModel> getSqlOperatorTags(String tableName, String service) throws SQLException {
        return db().table(tableName)
                .where("`service`=?",service)
                .groupBy("operator")
                .orderBy("operator ASC")
                .select("operator tag")
                .caching(CacheUtil.data).usingCache(60*3)
                .getList(new LogSqlModel());
    }
}
