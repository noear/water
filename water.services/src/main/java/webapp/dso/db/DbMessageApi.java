package webapp.dso.db;

import org.noear.water.tools.DisttimeUtil;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dso.CacheUtil;
import webapp.dso.IDUtil;
import webapp.model.ConfigModel;
import webapp.model.MessageModel;
import webapp.model.SubscribeModel;
import webapp.model.TopicModel;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by yuety on 2017/7/17.
 */
public final class DbMessageApi {

    private static DbContext db(){
        return Config.water_msg;
    }

    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicID(String topic) throws SQLException{
        TopicModel m = db().table("water_msg_topic")
                           .where("topic_name=?",topic)
                           .select("*")
                           .caching(CacheUtil.data)
                           .getItem(TopicModel.class);

        if(m.topic_id == 0){
            m.topic_id = (int)(db().table("water_msg_topic")
                    .set("topic_name",topic)
                    .insert());
        }

        return m;
    }

    //检查是否有订阅者(key,topic)
    public static boolean hasSubscriber(String key, String topic) throws SQLException {
        TopicModel m = getTopicID(topic);

        return db().table("water_msg_subscribe")
                .where("subscriber_key=?", key)
                .and("topic_id=?", m.topic_id)
                .exists();
    }

    //删除订阅者
    public static boolean removeSubscriber(String key,String topic) throws SQLException {
        TopicModel m = getTopicID(topic);

        return db().table("water_msg_subscribe")
                .where("subscriber_key=?", key).and("topic_id=?", m.topic_id)
                .delete() > 0;
    }

    //添加订阅者
    public static long addSubscriber(String key,String note,String alarm_mobile, String topic, String receive_url,String access_key, int receive_way) throws SQLException {
        TopicModel m = getTopicID(topic);

        return db().table("water_msg_subscribe").usingExpr(true)
                .set("alarm_mobile", alarm_mobile)
                .set("subscriber_key", key)
                .set("subscriber_note", note)
                .set("topic_id", m.topic_id)
                .set("topic_name", topic)
                .set("receive_url", receive_url)
                .set("access_key", access_key)
                .set("receive_way", receive_way)
                .set("log_fulltime", "$NOW()")
                .insert();
    }

    //更新订阅者信息
    public static long udpSubscriber(String key,String note, String alarm_mobile,String topic, String receive_url,String access_key, int receive_way) throws SQLException {
        TopicModel m = getTopicID(topic);

        return db().table("water_msg_subscribe").usingExpr(true)
                .set("alarm_mobile", alarm_mobile)
                .set("receive_url", receive_url)
                .set("access_key", access_key)
                .set("subscriber_note", note)
                .set("receive_way", receive_way)
                .set("log_fulltime", "$NOW()")
                .where("subscriber_key=?", key).and("topic_id=?", m.topic_id)
                .update();
    }

    //检查是否已有消息（key）
    public static boolean hasMessage(String key)  throws SQLException {
        return db().table("water_msg_message").where("msg_key=?", key).exists();
    }

    //取消消息（key）
    public static void cancelMessage(String msg_key)  throws SQLException {
        db().table("water_msg_message")
                .set("state", -1)
                .where("msg_key=?", msg_key).update();
    }

    //消费消息（key）（设为成功）
    public static void succeedMessage(String msg_key)  throws SQLException {
        db().table("water_msg_message")
                .set("state", 2)
                .where("msg_key=?", msg_key).update();
    }

    //取消消息派发（key+subscriber_key）
    public static void cancelMsgDistribution(String msg_key, String subscriber_key)  throws SQLException {

        MessageModel msg = getMessage(msg_key);
        if (msg.msg_id == 0)
            return;

        SubscribeModel subs = getSubscriber(msg.topic_id, subscriber_key);

        db().table("water_msg_distribution").set("state", -1)
                .where("msg_id=? AND subscriber_id=?", msg.msg_id, subs.subscriber_id)
                .update();
    }


    //消费消息派发（key+subscriber_key）（设为成功）
    public static void succeedMsgDistribution(String msg_key, String subscriber_key)  throws SQLException {
        MessageModel msg = getMessage(msg_key);
        if (msg.msg_id == 0)
            return;

        SubscribeModel subs = getSubscriber(msg.topic_id, subscriber_key);

        db().table("water_msg_distribution").set("state", 2)
                .where("msg_id=? AND subscriber_id=?", msg.msg_id, subs.subscriber_id)
                .update();
    }

    //添加消息
    public static long addMessage(String key, String topic, String content, Date time) throws SQLException {
        TopicModel m = getTopicID(topic);

        //支持最多消息量的限制
        if (m.max_msg_num > 0) {
            long num = db().table("$.message").where("topic_id=? AND (state=0 OR state=1)", m.topic_id).count();

            if (num >= m.max_msg_num) {
                return 1;
            }
        }

        long msg_id = IDUtil.buildMsgID();

        db().table("water_msg_message").usingExpr(true)
                .set("msg_id", msg_id)
                .set("msg_key", key)
                .set("topic_id", m.topic_id)
                .set("topic_name", topic)
                .set("content", content)
                .set("log_date", "$DATE(NOW())")
                .set("log_fulltime", "$NOW()").expre((tb) -> {
            if (time != null) {
                tb.set("dist_ntime", DisttimeUtil.nextTime(time));
            }
        }).insert();

        if(time == null) {
            addMessageToQueue(msg_id);
        }

        return msg_id;
    }

    private static RedisX _redisX = null;
    private static String _lock = "";
    private static void tryQueueInit() {
        if (_redisX == null) {
            synchronized (_lock) {
                if (_redisX == null) {
                    try {
                        ConfigModel cfg = DbApi.getConfig("water", "water_redis");
                        _redisX = new RedisX(cfg.toProp(), 3);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void addMessageToQueue(long msg_id){
        tryQueueInit();

        if(_redisX == null){
            return;
        }

        try {
            String msg_key = msg_id + "";
            String msg_key_h = Config.water_msg_queue + "_" + msg_key;

            _redisX.open0((rs) -> {
                if (rs.key(msg_key_h).expire(30).lock()) {
                    rs.key(Config.water_msg_queue).listAdd(msg_key);
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static MessageModel getMessage(String msg_key) throws SQLException{
        if(TextUtils.isEmpty(msg_key)) {
            return new MessageModel();
        }
        else {
            return Config.water_msg.table("water_msg_message")
                    .where("msg_key=?",msg_key)
                    .select("*")
                    .caching(CacheUtil.data).usingCache(60)
                    .getItem(MessageModel.class);
        }
    }

    public static SubscribeModel getSubscriber1ByTopic(int topicID) throws SQLException{
        return db().table("water_msg_subscribe")
                        .where("topic_id=?",topicID)
                        .limit(1)
                        .select("*")
                        .getItem(SubscribeModel.class);
    }

    public static SubscribeModel getSubscriber(int topicID, String subscriber_key) throws SQLException {
        return db().table("water_msg_subscribe")
                        .where("topic_id=?", topicID).and("subscriber_key=?", subscriber_key)
                        .limit(1)
                        .select("*")
                        .caching(CacheUtil.data).usingCache(60)
                        .getItem(SubscribeModel.class);
    }
}
