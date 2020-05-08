package waterapp.dso.db;

import org.noear.water.utils.DisttimeUtil;
import org.noear.water.utils.LockUtils;
import org.noear.weed.DbContext;
import waterapp.Config;
import waterapp.dso.LogUtil;
import waterapp.models.water_msg.DistributionModel;
import waterapp.models.water_msg.MessageModel;
import waterapp.models.water_msg.SubscriberModel;
import waterapp.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 2017/7/18.
 */
public class DbWaterMsgApi {

    public static DbContext db() {
        return Config.water_msg;
    }

    //获取主题
    public static TopicModel getTopic(int topic_id) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .where("topic_id=?", topic_id)
                .select("*")
                .caching(Config.cache_data)
                .getItem(new TopicModel());

        return m;
    }

    //获取待派发的消息列表
    public static List<Long> getMessageList(int rows, long ntime) throws SQLException {
        return db().table("water_msg_message")
                .where("state=0 AND dist_nexttime<?", ntime)
                .orderBy("msg_id ASC")
                .limit(rows)
                .select("msg_id")
                .getArray("msg_id");
    }

    //获取某一条消息
    public static MessageModel getMessage(long msgID) throws SQLException {
        MessageModel m = db().table("water_msg_message")
                .where("msg_id=? AND state=0", msgID)
                .select("*")
                .getItem(new MessageModel());

        if (m.state != 0) {
            return null;
        } else {
            return m;
        }
    }

    //设置消息状态
    public static boolean setMessageState(long msgID, int state) {
        return setMessageState(msgID, state, 0);
    }

    //设置消息状态
    public static boolean setMessageState(long msgID, int state, long nexttime) {
        try {
            db().table("water_msg_message")
                    .set("state", state)
                    .build(tb -> {
                        if (state == 0) {
                            long ntime = DisttimeUtil.nextTime(1);
                            tb.set("dist_nexttime", ntime);
                            //可以检查处理中时间是否过长了？可手动恢复状态
                        }

                        if (nexttime > 0) {
                            tb.set("dist_nexttime", nexttime);
                        }
                    })
                    .where("msg_id=? AND (state=0 OR state=1)", msgID)
                    .update();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            LogUtil.error("msg", "setMessageState", msgID + "", ex);

            return false;
        }
    }

    //设置消息重试状态（过几秒后再派发）
    public static boolean setMessageRepet(MessageModel msg, int state) {
        try {
            msg.dist_count += 1;

            long ntime = DisttimeUtil.nextTime(msg.dist_count);

            db().table("water_msg_message").usingExpr(true)
                    .set("state", state)
                    .set("dist_nexttime", ntime)
                    .set("dist_count", "$dist_count+1")
                    .where("msg_id=? AND (state=0 OR state=1)", msg.msg_id)
                    .update();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();

            LogUtil.error("msg", "setMessageRepet", msg.msg_id + "", ex);

            return false;
        }
    }

    //根据主题获取订阅者
    public static Map<Integer, SubscriberModel> getSubscriberListByTopic(int topicID) throws SQLException {
        Map<Integer, SubscriberModel> map = new HashMap<>();

        List<SubscriberModel> list = db().table("water_msg_subscriber")
                .where("topic_id=? AND is_enabled=1", topicID)
                .select("*")
                .getList(new SubscriberModel());

        list.forEach(m -> {
            map.put(m.subscriber_id, m);
        });

        return map;
    }

    public static List<SubscriberModel> getSubscriberList() throws SQLException {
        List<SubscriberModel> list = db().table("water_msg_subscriber")
                .where("is_enabled=1")
                .select("*")
                .getList(new SubscriberModel());

        return list;
    }

    public static void delSubscriber(int subscriber_id) {
        if (subscriber_id > 0) {
            try {
                db().table("water_msg_subscriber")
                        .where("subscriber_id = ?", subscriber_id)
                        .delete();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void setSubscriberState(int subscriber_id, int check_state) {
        if (subscriber_id > 0) {
            try {
                db().table("water_msg_subscriber").usingExpr(true)
                        .set("check_last_state", check_state)
                        .build((tb) -> {
                            if (check_state == 200)
                                tb.set("check_error_num", 0);
                            else
                                tb.set("check_error_num", "$check_error_num+1");
                        })
                        .whereEq("subscriber_id", subscriber_id)
                        .update();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    //添加派发任务
    public static void addDistribution(long msgID, SubscriberModel subs) throws SQLException {
        String lock_key = "distribution_" + msgID + "_" + subs.subscriber_id;

        //尝试2秒的锁
        if (LockUtils.tryLock("watersev", lock_key, 2)) {

            boolean isExists = db().table("water_msg_distribution")
                    .where("msg_id=?", msgID).and("subscriber_id=?", subs.subscriber_id)
                    .hint("/*TDDL:MASTER*/")
                    .exists();

            if (isExists == false) {
                db().table("water_msg_distribution").usingExpr(true)
                        .set("msg_id", msgID)
                        .set("subscriber_id", subs.subscriber_id)
                        .set("alarm_mobile", subs.alarm_mobile)
                        .set("alarm_sign", subs.alarm_sign)
                        .set("receive_url", subs.receive_url)
                        .set("access_key", subs.access_key)
                        .set("receive_way", subs.receive_way)
                        .set("log_date", "$DATE(NOW())")
                        .set("log_fulltime", "$NOW()")
                        .insert();
            }
        }
    }

    //根据消息获取派发任务
    public static List<DistributionModel> getDistributionListByMsg(long msgID) throws SQLException {
        return db().table("water_msg_distribution")
                .where("msg_id=? AND (state=0 OR state=1)", msgID)
                .hint("/*TDDL:MASTER*/")
                .select("*")
                .getList(new DistributionModel());
    }

    //设置派发状态（成功与否）
    public static boolean setDistributionState(long msg_id, DistributionModel dist, int state) {
        try {
            db().table("water_msg_distribution")
                    .set("state", state)
                    .set("duration", dist._duration)
                    .where("msg_id=? and subscriber_id=? and state<>2", msg_id, dist.subscriber_id)
                    .update();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            LogUtil.error("msg", "setDistributionState", msg_id + "", ex);

            return false;
        }
    }
}
