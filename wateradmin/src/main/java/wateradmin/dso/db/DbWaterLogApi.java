package wateradmin.dso.db;

import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.weed.DbContext;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;

import java.util.List;

/**
 logM.tag = String.valueOf(seconds);//留空
 logM.tag1 = path; //秒数
 logM.tag2 = operator;
 logM.tag3 = method;
 logM.tag4 = "";
 logM.weight = interval; //=tag5, 毫秒数
 logM.group = schema; //=tag6
 logM.service = service;//=tag7
 * */
public class DbWaterLogApi {
    public static List<LogModel> getSqlLogsByPage(String tableName, String service, String method, int seconds, String operator, String path, long startLogId, long timestamp) throws Exception {
        StringBuilder tagx = new StringBuilder();
        tagx.append(""); //tag0
        tagx.append("@"); //tag1
        if (TextUtils.isNotEmpty(path)) {
            tagx.append(path);
        }
        tagx.append("@"); //tag2
        if (TextUtils.isNotEmpty(operator)) {
            tagx.append(operator);
        }
        tagx.append("@"); //tag3
        if (TextUtils.isNotEmpty(method)) {
            tagx.append(method);
        }
        tagx.append("@"); //tag4
        tagx.append("@"); //tag5
        if (seconds > 0) {
            tagx.append(seconds);
        }
        tagx.append("@"); //tag6
        tagx.append("@"); //tag7
        if (TextUtils.isNotEmpty(service)) {
            tagx.append(service);
        }

        List<LogModel> logList = ProtocolHub.getLogSource(tableName).query(tableName, 0, 50, tagx.toString(), startLogId, timestamp);

        return logList;

//        int start = (page - 1) * pageSize;
//
//        return db().table(tableName)
//                .where("1 = 1")
//                .build(tb -> {
//                    if (!TextUtils.isEmpty(service)) {
//                        tb.and("`service` = ?", service);
//                    }
//                    if (!TextUtils.isEmpty(method)) {
//                        tb.and("`method` = ?", method);
//                    }
//
//                    if (!TextUtils.isEmpty(operator)) {
//                        tb.and("`operator` = ?", operator);
//                    }
//
//                    if (!TextUtils.isEmpty(path)) {
//                        tb.and("`path` = ?", path);
//                    }
//
//                    if (seconds > 0) {
//                        tb.and("`seconds` >= ?", seconds);
//                    }
//
//                    if (log_date > 0) {
//                        tb.and("log_date = ?", log_date);
//                    }
//
//                    if (log_hour > 0) {
//                        tb.and("log_hour = ?", log_hour);
//                    }
//                })
//                .orderBy("log_id desc")
//                .limit(start, pageSize)
//                .select("*")
//                .getList(new LogSqlModel());
    }

    public static List<TagCountsM> getSqlServiceTags(String tableName) throws Exception {
        return ProtocolHub.getLogSource(tableName)
                .queryGroupCountBy(tableName, null, "service");
//        return db().table(tableName)
//                .where("1 = 1")
//                .groupBy("`service`")
//                .orderBy("`service` ASC")
//                .select("`service` tag")
//                .caching(CacheUtil.data).usingCache(60 * 3)
//                .getList(TagCountsModel.class);
    }

    public static List<TagCountsM> getSqlSecondsTags(String tableName, String service) throws Exception {
        return ProtocolHub.getLogSource(tableName)
                .queryGroupCountBy(tableName, service, "tag");//tag1=>seconds

//        return db().table(tableName)
//                .where("`service`=?", service)
//                .groupBy("seconds")
//                .orderBy("seconds ASC")
//                .select("seconds tag")
//                .caching(CacheUtil.data).usingCache(60 * 3)
//                .getList(new LogSqlModel());
    }

    public static List<TagCountsM> getSqlOperatorTags(String tableName, String service) throws Exception {

        return ProtocolHub.getLogSource(tableName)
                .queryGroupCountBy(tableName, service, "tag2");//tag3=>operator

//        return db().table(tableName)
//                .where("`service`=?", service)
//                .groupBy("operator")
//                .orderBy("operator ASC")
//                .select("operator tag")
//                .caching(CacheUtil.data).usingCache(60 * 3)
//                .getList(TagCountsModel.class);
    }
}
