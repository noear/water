package org.noear.water.protocol.solution;

import org.noear.solon.Utils;
import org.noear.water.log.Logger;
import org.noear.water.protocol.IdBuilder;
import org.noear.water.protocol.MessageSource;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.utils.DisttimeUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/2/3 created
 */
public class MessageSourceRdb implements MessageSource {
    DbContext _db;
    ICacheServiceEx _cache;
    IdBuilder _idBuilder;
    Logger log_msg;

    public MessageSourceRdb(DbContext db, ICacheServiceEx cache) {
        _db = db;
        _cache = cache;
    }

    /////////
    //for waterapi

    //检查是否已有消息（key）
    public boolean hasMessage(String msg_key) throws SQLException {
        if (TextUtils.isEmpty(msg_key)) {
            return false;
        } else {
            return _db.table("water_msg_message")
                    .where("msg_key=?", msg_key)
                    .caching(_cache)
                    .selectExists();
        }
    }

    //取消消息（key）
    public void cancelMessage(String msg_key) throws SQLException {
        _db.table("water_msg_message")
                .set("state", -1)
                .where("msg_key=?", msg_key)
                .update();
    }

    //消费消息（key）（设为成功）
    public void succeedMessage(String msg_key) throws SQLException {
        _db.table("water_msg_message")
                .set("state", 2)
                .where("msg_key=?", msg_key)
                .update();
    }

    //取消消息派发（key+subscriber_key）
    public void cancelMsgDistribution(String msg_key, String subscriber_key) throws SQLException {

        _db.table("water_msg_distribution").set("state", -1)
                .where("msg_key=? AND subscriber_key=?", msg_key, subscriber_key)
                .update();
    }


    //消费消息派发（key+subscriber_key）（设为成功）
    public void succeedMsgDistribution(String msg_key, String subscriber_key) throws SQLException {
        _db.table("water_msg_distribution").set("state", 2)
                .where("msg_key=? AND subscriber_key=?", msg_key, subscriber_key)
                .update();
    }

    public long addMessage(String topic, String content) throws Exception {
        return addMessage(null, null, null, topic, content, null);
    }

    //添加消息
    public long addMessage(String msg_key, String trace_id, String tags, String topic, String content, Date plan_time) throws Exception {
        long msg_id = _idBuilder.getMsgId();

        if (Utils.isEmpty(msg_key)) {
            msg_key = Utils.guid();
        }

        if (Utils.isEmpty(trace_id)) {
            trace_id = Utils.guid();
        }

        if (tags == null) {
            tags = "";
        }

        _db.table("water_msg_message").usingExpr(true)
                .set("msg_id", msg_id)
                .set("msg_key", msg_key)
                .set("tags", tags)
                .set("trace_id", trace_id)
                .set("topic_id", 0)
                .set("topic_name", topic)
                .set("content", content)
                .set("plan_time", plan_time)
                .set("log_date", "$DATE(NOW())")
                .set("log_fulltime", "$NOW()").build((tb) -> {
            if (plan_time != null) {
                tb.set("dist_nexttime", DisttimeUtils.distTime(plan_time));
            }
        }).insert();

        if (plan_time != null) {
            addMessageToQueue(msg_id);
        }

        return msg_id;
    }

    public void addMessageToQueue(long msg_id) throws Exception {
        String msg_id_str = msg_id + "";

        if (ProtocolHub.messageLock.lock(msg_id_str)) {
            ProtocolHub.messageQueue.push(msg_id_str);
        }
    }

    //////
    // for water sev

    //获取待派发的消息列表
    public List<Long> getMessageList(int rows, long dist_nexttime) throws SQLException {
        return _db.table("water_msg_message")
                .where("state=0 AND dist_nexttime<?", dist_nexttime)
                .orderBy("msg_id ASC")
                .limit(rows)
                .selectArray("msg_id");
    }

    //获取某一条消息
    public MessageModel getMessageOfPending(long msg_id) throws SQLException {
        MessageModel m = _db.table("water_msg_message")
                .where("msg_id=? AND state=0", msg_id)
                .selectItem("*", MessageModel.class);

        if (m.state != 0) {
            return null;
        } else {
            return m;
        }
    }

    public void setMessageRouteState(MessageModel msg, boolean dist_routed) {
        try {
            _db.table("water_msg_message")
                    .set("dist_routed", dist_routed)
                    .where("msg_id=?", msg.msg_id)
                    .update();

            msg.dist_routed = dist_routed;
        } catch (Exception ex) {
            //ex.printStackTrace();

            log_msg.error("", msg.msg_id + "", "setMessageRouteState", msg.msg_id + "", ex);
        }
    }

    /**
     * 设置消息状态
     *
     * @param state -2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数
     */
    public boolean setMessageState(MessageModel msg, MessageState state) {
        return setMessageState(msg, state, 0);
    }

    //设置消息状态
    public boolean setMessageState(MessageModel msg, MessageState state, long dist_nexttime) {
        try {
            _db.table("water_msg_message")
                    .set("state", state.code)
                    .build(tb -> {
                        if (state == MessageState.undefined) {
                            long ntime = DisttimeUtils.nextTime(1);
                            tb.set("dist_nexttime", ntime);
                            //可以检查处理中时间是否过长了？可手动恢复状态
                        }

                        if (dist_nexttime > 0) {
                            tb.set("dist_nexttime", dist_nexttime);
                        }
                    })
                    .where("msg_id=? AND (state=0 OR state=1)", msg.msg_id)
                    .update();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            log_msg.error(msg.topic_name, msg.msg_id + "", "setMessageState", "", ex);

            return false;
        }
    }

    //设置消息重试状态（过几秒后再派发）
    public boolean setMessageRepet(MessageModel msg, MessageState state) {
        try {
            msg.dist_count += 1;

            long ntime = DisttimeUtils.nextTime(msg.dist_count);

            _db.table("water_msg_message").usingExpr(true)
                    .set("state", state.code)
                    .set("dist_nexttime", ntime)
                    .set("dist_count", "$dist_count+1")
                    .where("msg_id=? AND (state=0 OR state=1)", msg.msg_id)
                    .update();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();

            log_msg.error(msg.topic_name, msg.msg_id + "", "setMessageRepet", msg.msg_id + "", ex);

            return false;
        }
    }


    //添加派发任务
    public void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws SQLException {
//        String lock_key = "distribution_" + msg.msg_id + "_" + subs.subscriber_id;

        //尝试2秒的锁
//        if (LockUtils.tryLock("watersev", lock_key, 2)) {

        //过滤时间还要用一下
//            boolean isExists = db().table("water_msg_distribution")
//                    .where("msg_id=?", msg.msg_id).and("subscriber_id=?", subs.subscriber_id)
//                    .hint("/*TDDL:MASTER*/")
//                    .selectExists();
//
//            if (isExists == false) {
        _db.table("water_msg_distribution").usingExpr(true)
                .set("msg_id", msg.msg_id)
                .set("msg_key", msg.msg_key)
                .set("subscriber_id", subs.subscriber_id)
                .set("subscriber_key", subs.subscriber_key)
                .set("alarm_mobile", subs.alarm_mobile)
                .set("alarm_sign", subs.alarm_sign)
                .set("receive_url", subs.receive_url)
                .set("receive_key", subs.receive_key)
                .set("receive_way", subs.receive_way)
                .set("log_date", "$DATE(NOW())")
                .set("log_fulltime", "$NOW()")
                .insert();
//            }
//        }
    }

    //根据消息获取派发任务
    public List<DistributionModel> getDistributionListByMsg(long msg_id) throws SQLException {
        return _db.table("water_msg_distribution")
                .where("msg_id=? AND (state=0 OR state=1)", msg_id)
                .hint("/*TDDL:MASTER*/")
                .caching(_cache).usingCache(60)
                .selectList("*", DistributionModel.class);
    }

    //设置派发状态（成功与否）
    public boolean setDistributionState(MessageModel msg, DistributionModel dist, MessageState state) {
        try {
            _db.table("water_msg_distribution")
                    .set("state", state.code)
                    .set("duration", dist._duration)
                    .where("msg_id=? and subscriber_id=? and state<>2", msg.msg_id, dist.subscriber_id)
                    .update();

            return true;
        } catch (Throwable ex) {

            log_msg.error(msg.topic_name, msg.msg_id + "", "setDistributionState", "", ex);

            return false;
        }
    }
}
