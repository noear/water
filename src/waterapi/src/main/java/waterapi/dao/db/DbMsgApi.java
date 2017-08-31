package waterapi.dao.db;

import noear.weed.*;
import org.apache.http.util.TextUtils;
import waterapi.Config;
import waterapi.dao.CacheUtil;
import waterapi.dao.DisttimeUtil;
import waterapi.dao.IDUtil;
import waterapi.utils.DateUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by yuety on 2017/7/17.
 */
public final class DbMsgApi {

    private static DbContext db(){
        return Config.water_msg;
    }

    public static int getTopicID(String topic) throws SQLException{
        int topic_id = db().table("topic")
                           .where("topic_name=?",topic)
                           .select("topic_id")
                           .caching(CacheUtil.data)
                           .getValue(0);

        if(topic_id == 0){
            topic_id = (int)(db().table("topic")
                    .set("topic_name",topic)
                    .insert());
        }

        return topic_id;
    }

    public static boolean hasSubscriber(String key, String topic) throws SQLException{
        int topic_id = getTopicID(topic);

        return db().table("subscriber")
                   .where("subscriber_key=?",key)
                   .and("topic_id=?",topic_id)
                   .exists();
    }

    public static boolean removeSubscriber(String key,String topic) throws SQLException {
        int topic_id = getTopicID(topic);

        return
        db().table("subscriber")
            .where("subscriber_key=?", key).and("topic_id=?", topic_id)
            .delete() > 0;
    }

    public static long addSubscriber(String key,String alarm_mobile, String topic, String receive_url,String access_key, boolean is_sync) throws SQLException {
        int topic_id = getTopicID(topic);

        return
                db().table("subscriber")
                        .set("alarm_mobile",alarm_mobile)
                        .set("subscriber_key", key)
                        .set("topic_id", topic_id)
                        .set("topic_name", topic)
                        .set("receive_url", receive_url)
                        .set("access_key",access_key)
                        .set("is_sync", (is_sync ? 1 : 0))
                        .set("log_fulltime", "$NOW()")
                        .insert();
    }

    public static long udpSubscriber(String key, String alarm_mobile,String topic, String receive_url,String access_key, boolean is_sync) throws SQLException {
        int topic_id = getTopicID(topic);

        return
                db().table("subscriber")
                        .set("alarm_mobile",alarm_mobile)
                        .set("receive_url", receive_url)
                        .set("access_key",access_key)
                        .set("is_sync", (is_sync ? 1 : 0))
                        .set("log_fulltime", "$NOW()")
                        .where("subscriber_key=?", key).and("topic_id=?", topic_id)
                        .update();
    }


    public static boolean hasMessage(String key)  throws SQLException{
        return db().table("message").where("msg_key=?",key).exists();
    }

    public static void cancelMessage(String key)  throws SQLException{
        db().table("message").set("state",-1).where("msg_key=?",key).update();
    }

    public static long addMessage(String key, String topic, String content, Date time) throws SQLException {
        int topic_id = getTopicID(topic);

        return db().table("message")
                .set("msg_id", IDUtil.buildMsgID())
                .set("msg_key", key)
                .set("topic_id", topic_id)
                .set("topic_name", topic)
                .set("content", content)
                .set("log_date", "$DATE(NOW())")
                .set("log_fulltime", "$NOW()").expre((tb) -> {
                    if (time != null) {
                        tb.set("dist_ntime", DisttimeUtil.nextTime(time));
                    }
                })
                .insert();
    }

    public static void setState1As0() throws SQLException {
         db().table("message")
                .set("state", 0)
                .where("state=?",1)
                .update();
    }

    public static void clean(int state) throws SQLException {
        if(state == 0 || state==1){
            return;
        }

        db().table("distribution")
                .where(" msg_id IN (select msg_id from message where state=?)",state)
                .delete();

        db().table("message")
                .where("state=?",state)
                .delete();
    }

    public static DataList getMessageList(int dist_count, int topic_id) throws SQLException{
        if(dist_count == 0 && topic_id ==0) {
            return new DataList();
        }
        else {
            return Config.water_msg.table("message").expre((tb) -> {
                tb.where("state=0");
                if (dist_count > 0) {
                    tb.and("dist_count>?", dist_count);
                } else {
                    tb.and("topic_id=?", topic_id);
                }
            }).orderBy("msg_id ASC").limit(50).select("*").getDataList();
        }
    }
}
