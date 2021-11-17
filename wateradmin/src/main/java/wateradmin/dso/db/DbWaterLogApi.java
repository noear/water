package wateradmin.dso.db;

import org.noear.water.model.TagCountsM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.log.LogModel;
import org.noear.water.utils.TextUtils;

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
    }

    public static List<TagCountsM> getSqlServiceTags(String tableName) throws Exception {
        return ProtocolHub.getLogSource(tableName)
                .queryGroupCountBy(tableName, null, "service");
    }

    public static List<TagCountsM> getSqlSecondsTags(String tableName, String service) throws Exception {
        return ProtocolHub.getLogSource(tableName)
                .queryGroupCountBy(tableName, service, "tag");//tag1=>seconds
    }

    public static List<TagCountsM> getSqlOperatorTags(String tableName, String service) throws Exception {
        return ProtocolHub.getLogSource(tableName)
                .queryGroupCountBy(tableName, service, "tag2");//tag3=>operator
    }
}
