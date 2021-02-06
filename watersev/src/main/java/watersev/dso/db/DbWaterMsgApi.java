package watersev.dso.db;

import org.noear.water.log.Logger;
import org.noear.water.log.WaterLogger;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.weed.DbContext;
import watersev.Config;
import watersev.models.water_msg.TopicModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 2017/7/18.
 */
public class DbWaterMsgApi {
    private static Logger log_msg = WaterLogger.get("water_log_msg");

    public static DbContext db() {
        return Config.water_msg;
    }

    //获取主题
    public static TopicModel getTopic(String topic) throws SQLException {
        TopicModel m = db().table("water_msg_topic")
                .where("topic_name=?", topic)
                .caching(Config.cache_data)
                .selectItem("*", TopicModel.class);

        return m;
    }

    //根据主题获取订阅者
    public static Map<Integer, SubscriberModel> getSubscriberListByTopic(String topic_name) throws SQLException {
        Map<Integer, SubscriberModel> map = new HashMap<>();

        List<SubscriberModel> list = db().table("water_msg_subscriber")
                .where("topic_name=? AND is_enabled=1", topic_name)
                .caching(Config.cache_data).usingCache(30)
                .selectList("*", SubscriberModel.class);

        list.forEach(m -> {
            map.put(m.subscriber_id, m);
        });

        return map;
    }

    public static List<SubscriberModel> getSubscriberListNoCache() throws SQLException {
        List<SubscriberModel> list = db().table("water_msg_subscriber")
                .where("is_enabled=1")
                .caching(Config.cache_data)
                .selectList("*", SubscriberModel.class);

        return list;
    }

    public static void delSubscriber(int subscriber_id) {
        if (subscriber_id > 0) {
            try {
                db().table("water_msg_subscriber")
                        .where("subscriber_id = ?", subscriber_id)
                        .delete();
            } catch (Throwable ex) {
                //EventBus.pushAsyn(ex);
                log_msg.error("delSubscriber", "", ex);
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
                //ex.printStackTrace();
                log_msg.error("setSubscriberState", subscriber_id + "", ex);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

//    //获取待派发的消息列表
//    public static List<Long> getMessageList(int rows, long dist_nexttime) throws SQLException {
//        return db().table("water_msg_message")
//                .where("state=0 AND dist_nexttime<?", dist_nexttime)
//                .orderBy("msg_id ASC")
//                .limit(rows)
//                .selectArray("msg_id");
//    }
//
//    //获取某一条消息
//    public static MessageModel getMessageOfPending(long msg_id) throws SQLException {
//        MessageModel m = db().table("water_msg_message")
//                .where("msg_id=? AND state=0", msg_id)
//                .selectItem("*", MessageModel.class);
//
//        if (m.state != 0) {
//            return null;
//        } else {
//            return m;
//        }
//    }
//
//    public static void setMessageRouteState(MessageModel msg, boolean dist_routed) {
//        try {
//            db().table("water_msg_message")
//                    .set("dist_routed", dist_routed)
//                    .where("msg_id=?", msg.msg_id)
//                    .update();
//
//            msg.dist_routed = dist_routed;
//        } catch (Exception ex) {
//            //ex.printStackTrace();
//
//            log_msg.error("", msg.msg_id + "", "setMessageRouteState", msg.msg_id + "", ex);
//        }
//    }
//
//    /**
//     * 设置消息状态
//     *
//     * @param state -2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数
//     */
//    public static boolean setMessageState(MessageModel msg, MessageState state) {
//        return setMessageState(msg, state, 0);
//    }
//
//    //设置消息状态
//    public static boolean setMessageState(MessageModel msg, MessageState state, long dist_nexttime) {
//        try {
//            db().table("water_msg_message")
//                    .set("state", state.code)
//                    .build(tb -> {
//                        if (state == MessageState.undefined) {
//                            long ntime = DisttimeUtils.nextTime(1);
//                            tb.set("dist_nexttime", ntime);
//                            //可以检查处理中时间是否过长了？可手动恢复状态
//                        }
//
//                        if (dist_nexttime > 0) {
//                            tb.set("dist_nexttime", dist_nexttime);
//                        }
//                    })
//                    .where("msg_id=? AND (state=0 OR state=1)", msg.msg_id)
//                    .update();
//
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//            log_msg.error(msg.topic_name, msg.msg_id + "", "setMessageState",  "", ex);
//
//            return false;
//        }
//    }
//
//    //设置消息重试状态（过几秒后再派发）
//    public static boolean setMessageRepet(MessageModel msg, MessageState state) {
//        try {
//            msg.dist_count += 1;
//
//            long ntime = DisttimeUtils.nextTime(msg.dist_count);
//
//            db().table("water_msg_message").usingExpr(true)
//                    .set("state", state.code)
//                    .set("dist_nexttime", ntime)
//                    .set("dist_count", "$dist_count+1")
//                    .where("msg_id=? AND (state=0 OR state=1)", msg.msg_id)
//                    .update();
//
//            return true;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//
//            log_msg.error(msg.topic_name, msg.msg_id + "", "setMessageRepet", msg.msg_id + "", ex);
//
//            return false;
//        }
//    }
//
//
//    //添加派发任务
//    public static void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws SQLException {
////        String lock_key = "distribution_" + msg.msg_id + "_" + subs.subscriber_id;
//
//        //尝试2秒的锁
////        if (LockUtils.tryLock("watersev", lock_key, 2)) {
//
//            //过滤时间还要用一下
////            boolean isExists = db().table("water_msg_distribution")
////                    .where("msg_id=?", msg.msg_id).and("subscriber_id=?", subs.subscriber_id)
////                    .hint("/*TDDL:MASTER*/")
////                    .selectExists();
////
////            if (isExists == false) {
//                db().table("water_msg_distribution").usingExpr(true)
//                        .set("msg_id", msg.msg_id)
//                        .set("msg_key", msg.msg_key)
//                        .set("subscriber_id", subs.subscriber_id)
//                        .set("subscriber_key", subs.subscriber_key)
//                        .set("alarm_mobile", subs.alarm_mobile)
//                        .set("alarm_sign", subs.alarm_sign)
//                        .set("receive_url", subs.receive_url)
//                        .set("receive_key", subs.receive_key)
//                        .set("receive_way", subs.receive_way)
//                        .set("log_date", "$DATE(NOW())")
//                        .set("log_fulltime", "$NOW()")
//                        .insert();
////            }
////        }
//    }
//
//    //根据消息获取派发任务
//    public static List<DistributionModel> getDistributionListByMsg(long msg_id) throws SQLException {
//        return db().table("water_msg_distribution")
//                .where("msg_id=? AND (state=0 OR state=1)", msg_id)
//                .hint("/*TDDL:MASTER*/")
//                .caching(Config.cache_data).usingCache(60)
//                .selectList("*", DistributionModel.class);
//    }
//
//    //设置派发状态（成功与否）
//    public static boolean setDistributionState(MessageModel msg, DistributionModel dist, MessageState state) {
//        try {
//            db().table("water_msg_distribution")
//                    .set("state", state.code)
//                    .set("duration", dist._duration)
//                    .where("msg_id=? and subscriber_id=? and state<>2", msg.msg_id, dist.subscriber_id)
//                    .update();
//
//            return true;
//        } catch (Throwable ex) {
//
//            log_msg.error(msg.topic_name, msg.msg_id + "", "setDistributionState", "", ex);
//
//            return false;
//        }
//    }
}
