package waterapi.dso.db;

import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.DisttimeUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.*;
import waterapi.dso.IDUtils;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.models.TopicModel;

import java.sql.SQLException;
import java.util.Date;

/**
 * 消息服务接口
 * */
public final class DbWaterMsgApi {

    private static DbContext db() {
        return Config.water_msg;
    }

    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicID(String topic) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .where("topic_name=?", topic)
                .caching(CacheUtils.data)
                .selectItem("*", TopicModel.class);

        if (m.topic_id == 0) {
            m.topic_id = (int) (db().table("water_msg_topic")
                    .set("topic_name", topic)
                    .insert());
        }

        return m;
    }


    //删除订阅者
    public static boolean removeSubscriber(String key, String topic) throws SQLException {
        TopicModel m = getTopicID(topic);

        return db().table("water_msg_subscriber")
                .where("subscriber_key=?", key).and("topic_id=?", m.topic_id)
                .delete() > 0;
    }

    //添加订阅者
    public static long addSubscriber(String key, String note, String alarm_mobile, String topic, String receive_url, String receive_key, int receive_way, boolean is_unstable) throws SQLException {
        TopicModel m = getTopicID(topic);


        DbTableQuery tq = db().table("water_msg_subscriber").usingExpr(true)
                .set("alarm_mobile", alarm_mobile)
                .set("is_unstable", (is_unstable ? 1 : 0))
                .set("subscriber_key", key)
                .set("subscriber_note", note)
                .set("receive_url", receive_url)
                .set("receive_key", receive_key)//后面要改掉
                .set("receive_way", receive_way)
                .set("log_fulltime", "$NOW()");

        if (db().table("water_msg_subscriber")
                .where("subscriber_key=?", key).and("topic_id=?", m.topic_id)
                .selectExists()) {
            return tq.whereEq("subscriber_key", key)
                    .andEq("topic_id", m.topic_id)
                    .update();
        } else {
            return tq.set("topic_id", m.topic_id)
                    .set("topic_name", topic).insert();
        }
    }



//    public static void addMessageToQueue(long msg_id) throws Exception {
//        if (Config.rd_msg == null) {
//            return;
//        }
//
//        String msg_id_str = msg_id + "";
//
//        if (ProtocolHub.messageLock.lock(msg_id_str)) {
//            ProtocolHub.messageQueue.push(msg_id_str);
//        }
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

//    //检查是否已有消息（key）
//    public static boolean hasMessage(String key) throws SQLException {
//        if (TextUtils.isEmpty(key)) {
//            return false;
//        } else {
//            return db().table("water_msg_message")
//                    .where("msg_key=?", key)
//                    .caching(CacheUtils.data)
//                    .selectExists();
//        }
//    }
//
//    //取消消息（key）
//    public static void cancelMessage(String msg_key) throws SQLException {
//        db().table("water_msg_message")
//                .set("state", -1)
//                .where("msg_key=?", msg_key)
//                .update();
//    }
//
//    //消费消息（key）（设为成功）
//    public static void succeedMessage(String msg_key) throws SQLException {
//        db().table("water_msg_message")
//                .set("state", 2)
//                .where("msg_key=?", msg_key)
//                .update();
//    }
//
//    //取消消息派发（key+subscriber_key）
//    public static void cancelMsgDistribution(String msg_key, String subscriber_key) throws SQLException {
//
//        db().table("water_msg_distribution").set("state", -1)
//                .where("msg_key=? AND subscriber_key=?", msg_key, subscriber_key)
//                .update();
//    }
//
//
//    //消费消息派发（key+subscriber_key）（设为成功）
//    public static void succeedMsgDistribution(String msg_key, String subscriber_key) throws SQLException {
//        db().table("water_msg_distribution").set("state", 2)
//                .where("msg_key=? AND subscriber_key=?", msg_key, subscriber_key)
//                .update();
//    }
//
//    public static long addMessage(String topic, String content) throws Exception {
//        return addMessage(null, null, null, topic, content, null);
//    }
//
//    //添加消息
//    public static long addMessage(String key, String trace_id, String tags, String topic, String content, Date plan_time) throws Exception {
//        TopicModel m = getTopicID(topic);
//
//        if (TextUtils.isEmpty(key)) {
//            key = IDUtils.buildGuid();
//        }
//
//        //支持最多消息量的限制
//        if (m.max_msg_num > 0) {
//            long num = db().table("water_msg_message")
//                    .where("topic_id=? AND (state=0 OR state=1)", m.topic_id)
//                    .selectCount();
//
//            if (num >= m.max_msg_num) {
//                return 1;
//            }
//        }
//
//        long msg_id = IDUtils.buildMsgID();
//
//        db().table("water_msg_message").usingExpr(true)
//                .set("msg_id", msg_id)
//                .set("msg_key", key)
//                .setDf("tags", tags, "")
//                .setIf(TextUtils.isNotEmpty(trace_id), "trace_id", trace_id)
//                .set("topic_id", m.topic_id)
//                .set("topic_name", topic)
//                .set("content", content)
//                .set("plan_time", plan_time)
//                .set("log_date", "$DATE(NOW())")
//                .set("log_fulltime", "$NOW()").build((tb) -> {
//            if (plan_time != null) {
//                tb.set("dist_nexttime", DisttimeUtils.distTime(plan_time));
//            }
//        }).insert();
//
//        if (plan_time == null) {
//            addMessageToQueue(msg_id);
//        }
//
//        return msg_id;
//    }
}
