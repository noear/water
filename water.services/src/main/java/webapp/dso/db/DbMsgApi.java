package webapp.dso.db;

import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.dao.CacheUtil;
import waterapi.dao.DisttimeUtil;
import waterapi.dao.IDUtil;
import waterapi.dao.WaterCfg;
import waterapi.models.MessageModel;
import waterapi.models.SubscriberModel;
import waterapi.models.TopicModel;
import waterapi.utils.RedisX;
import waterapi.utils.TextUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuety on 2017/7/17.
 */
public final class DbMsgApi {

    private static DbContext db(){
        return Config.water_msg;
    }

    //获取主题ID（没有则创建一个）
    public static TopicModel getTopicID(String topic) throws SQLException{
        TopicModel m = db().table("$.topic")
                           .where("topic_name=?",topic)
                           .select("*")
                           .caching(CacheUtil.data)
                           .getItem(new TopicModel());

        if(m.topic_id == 0){
            m.topic_id = (int)(db().table("$.topic")
                    .set("topic_name",topic)
                    .insert());
        }

        return m;
    }

    //检查是否有订阅者(key,topic)
    public static boolean hasSubscriber(String key, String topic) throws SQLException{
        TopicModel m = getTopicID(topic);

        return db().table("$.subscriber")
                   .where("subscriber_key=?",key)
                   .and("topic_id=?",m.topic_id)
                   .exists();
    }

    //删除订阅者
    public static boolean removeSubscriber(String key,String topic) throws SQLException {
        TopicModel m = getTopicID(topic);

        return
        db().table("$.subscriber")
            .where("subscriber_key=?", key).and("topic_id=?", m.topic_id)
            .delete() > 0;
    }

    //添加订阅者
    public static long addSubscriber(String key,String note,String alarm_mobile, String topic, String receive_url,String access_key, int receive_way) throws SQLException {
        TopicModel m = getTopicID(topic);

        return
                db().table("$.subscriber").usingExpr(true)
                        .set("alarm_mobile",alarm_mobile)
                        .set("subscriber_key", key)
                        .set("subscriber_note", note)
                        .set("topic_id", m.topic_id)
                        .set("topic_name", topic)
                        .set("receive_url", receive_url)
                        .set("access_key",access_key)
                        .set("receive_way", receive_way)
                        .set("log_fulltime", "$NOW()")
                        .insert();
    }

    //更新订阅者信息
    public static long udpSubscriber(String key,String note, String alarm_mobile,String topic, String receive_url,String access_key, int receive_way) throws SQLException {
        TopicModel m = getTopicID(topic);

        return
                db().table("$.subscriber").usingExpr(true)
                        .set("alarm_mobile",alarm_mobile)
                        .set("receive_url", receive_url)
                        .set("access_key",access_key)
                        .set("subscriber_note",note)
                        .set("receive_way", receive_way)
                        .set("log_fulltime", "$NOW()")
                        .where("subscriber_key=?", key).and("topic_id=?", m.topic_id)
                        .update();
    }

    //检查是否已有消息（key）
    public static boolean hasMessage(String key)  throws SQLException{
        return db().table("$.message").where("msg_key=?",key).exists();
    }

    //取消消息（key）
    public static void cancelMessage(String msg_key)  throws SQLException{
        db().table("$.message")
                .set("state",-1)
                .where("msg_key=?",msg_key).update();
    }

    //消费消息（key）（设为成功）
    public static void succeedMessage(String msg_key)  throws SQLException {
        db().table("$.message")
                .set("state",2)
                .where("msg_key=?",msg_key).update();
    }

    //取消消息派发（key+subscriber_key）
    public static void cancelMsgDistribution(String msg_key, String subscriber_key)  throws SQLException{

       MessageModel msg = getMessage(msg_key);
       if(msg.msg_id==0)
           return;

       SubscriberModel subs = getSubscriber(msg.topic_id, subscriber_key);

        db().table("$.distribution").set("state",-1)
                .where("msg_id=? AND subscriber_id=?",msg.msg_id, subs.subscriber_id).update();
    }


    //消费消息派发（key+subscriber_key）（设为成功）
    public static void succeedMsgDistribution(String msg_key, String subscriber_key)  throws SQLException {
        MessageModel msg = getMessage(msg_key);
        if (msg.msg_id == 0)
            return;

        SubscriberModel subs = getSubscriber(msg.topic_id, subscriber_key);

        db().table("$.distribution").set("state", 2)
                .where("msg_id=? AND subscriber_id=?", msg.msg_id, subs.subscriber_id).update();
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

        db().table("$.message").usingExpr(true)
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
    private static ReentrantLock lock = new ReentrantLock();
    private static void tryQueueInit() {
        if (_redisX == null) {
            lock.lock();

            if (_redisX == null) {
                try {
                    WaterCfg.ConfigModel cfg = WaterCfg.Config.get("water_redis");
                    _redisX = new RedisX(cfg.url, cfg.user, cfg.password, 3);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            lock.unlock();
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

            _redisX.open((rs) -> {
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
            return Config.water_msg.table("$.message")
                    .where("msg_key=?",msg_key)
                    .select("*")
                    .caching(CacheUtil.data).usingCache(60)
                    .getItem(new MessageModel());
        }
    }

    public static SubscriberModel getSubscriber1ByTopic(int topicID) throws SQLException{
        return db().table("$.subscriber")
                        .where("topic_id=?",topicID)
                        .limit(1)
                        .select("*")
                        .getItem(new SubscriberModel());
    }

    public static SubscriberModel getSubscriber(int topicID, String subscriber_key) throws SQLException {
        return db().table("$.subscriber")
                        .where("topic_id=?", topicID).and("subscriber_key=?", subscriber_key)
                        .limit(1)
                        .select("*")
                        .caching(CacheUtil.data).usingCache(60)
                        .getItem(new SubscriberModel());
    }
}
