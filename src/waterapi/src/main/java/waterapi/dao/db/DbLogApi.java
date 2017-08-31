package waterapi.dao.db;

import noear.weed.DataList;
import org.apache.http.util.TextUtils;
import waterapi.Config;
import waterapi.dao.IDUtil;

import java.sql.SQLException;

/**
 * Created by yuety on 2017/7/18.
 */
public final class DbLogApi {
    public static void addLog(String logger,  String tag, long tag1, long tag2, String label, String content) throws SQLException {
        Config.water_log.table(logger)
                .set("log_id", IDUtil.buildLogID())
                .set("tag", tag)
                .set("tag1", tag1)
                .set("tag2", tag2)
                .set("label", label)
                .set("content", content)
                .set("log_date", "$DATE(NOW())")
                .set("log_fulltime", "$NOW()")
                .insert();
    }

    public static DataList getLogList(String logger, String tag,long tag1,long tag2, int date, long start_id) throws SQLException {
        return
                Config.water_log_r.table(logger).expre((tb) -> {
                            tb.where("1=1");
                            if (start_id > 0) {
                                tb.and("log_id<=?", start_id);
                            }

                            if (date > 0) {
                                tb.and("log_date=?", date);
                            }

                            if (TextUtils.isEmpty(tag) == false) {
                                tb.and("tag=?", tag);

                                if(tag1>0){
                                    tb.and("tag1=?", tag1);
                                }

                                if(tag2>0){
                                    tb.and("tag2=?", tag2);
                                }
                            }
                        })
                        .orderBy("log_id desc").limit(50)
                        .select("*")
                        .getDataList();
    }

    public static void newLogger(String logger) throws SQLException{
        StringBuilder sb = new StringBuilder(200);

        sb.append("CREATE TABLE IF NOT EXISTS `"+logger+"` (");
        sb.append(" `log_id` bigint(20) NOT NULL AUTO_INCREMENT,");
        sb.append(" `level` int(11) NOT NULL DEFAULT '0',");
        sb.append(" `tag` varchar(100) NOT NULL COMMENT '标签',");
        sb.append(" `label` varchar(1000) DEFAULT NULL,");
        sb.append(" `content` text COMMENT '内容',");
        sb.append(" `log_date` int(11) NOT NULL DEFAULT '0',");
        sb.append(" `log_fulltime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,");
        sb.append(" PRIMARY KEY (`log_id`)");

        if(Config.is_debug) {
            sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        }else{
            sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 DBPARTITION BY hash(log_id);");
        }

        Config.water_log.sql(sb.toString()).execute();
    }
}
