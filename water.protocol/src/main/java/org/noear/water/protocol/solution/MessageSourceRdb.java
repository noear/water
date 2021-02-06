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
import org.noear.water.utils.Datetime;
import org.noear.water.utils.DisttimeUtils;
import org.noear.water.utils.StringUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.weed.cache.ICacheServiceEx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/2/3 created
 */
public class MessageSourceRdb implements MessageSource {
    DbContext _db;
    ICacheServiceEx _cache;
    Logger _logMsg;

    public MessageSourceRdb(DbContext db, ICacheServiceEx cache, Logger log) {
        _db = db;
        _cache = cache;
        _logMsg = log;
    }

    /////////
    //for waterapi

    //检查是否已有消息（key）
    public boolean hasMessage(String msg_key) throws SQLException {
        if (TextUtils.isEmpty(msg_key)) {
            return false;
        } else {
            return _db.table("water_msg_message")
                    .whereEq("msg_key", msg_key)
                    .caching(_cache)
                    .selectExists();
        }
    }

    //取消消息（key）
    public void setMessageAsCancel(String msg_key) throws SQLException {
        _db.table("water_msg_message")
                .set("state", -1)
                .whereEq("msg_key", msg_key)
                .update();
    }

    //消费消息（key）（设为成功）
    public void setMessageAsSucceed(String msg_key) throws SQLException {
        _db.table("water_msg_message")
                .set("state", 2)
                .whereEq("msg_key", msg_key)
                .update();
    }

    //取消消息派发（key+subscriber_key）
    public void setDistributionAsCancel(String msg_key, String subscriber_key) throws SQLException {

        _db.table("water_msg_distribution").set("state", -1)
                .whereEq("msg_key", msg_key).andEq("subscriber_key", subscriber_key)
                .update();
    }


    //消费消息派发（key+subscriber_key）（设为成功）
    public void setDistributionAsSucceed(String msg_key, String subscriber_key) throws SQLException {
        _db.table("water_msg_distribution").set("state", 2)
                .whereEq("msg_key", msg_key).andEq("subscriber_key", subscriber_key)
                .update();
    }

    public long addMessage(int topic_id, String topic_name, String content) throws Exception {
        return addMessage(null, null, null, topic_id, topic_name, content, null);
    }

    //添加消息
    public long addMessage(String msg_key, String trace_id, String tags, int topic_id, String topic_name, String content, Date plan_time) throws Exception {
        long msg_id = ProtocolHub.idBuilder.getMsgId();

        if (Utils.isEmpty(msg_key)) {
            msg_key = Utils.guid();
        }

        if (Utils.isEmpty(trace_id)) {
            trace_id = Utils.guid();
        }

        if (tags == null) {
            tags = "";
        }

        long dist_nexttime = 0;
        if (plan_time != null) {
            dist_nexttime = DisttimeUtils.distTime(plan_time);
        }

        Datetime datetime = new Datetime();

        _db.table("water_msg_message")
                .set("msg_id", msg_id)
                .set("msg_key", msg_key)
                .set("tags", tags)
                .set("trace_id", trace_id)
                .set("topic_id", topic_id)
                .set("topic_name", topic_name)
                .set("content", content)
                .set("plan_time", plan_time)
                .set("log_date", datetime.getDate())
                .set("log_fulltime", datetime.getFulltime())
                .set("last_fulltime", datetime.getFulltime())
                .set("dist_nexttime", dist_nexttime)
                .insert();

        return msg_id;
    }

    //////
    // for water sev

    //获取待派发的消息列表
    public List<MessageModel> getMessageListOfPending(int rows, long dist_nexttime) throws SQLException {
        return _db.table("water_msg_message")
                .whereEq("state", 0).andLt("dist_nexttime", dist_nexttime)
                .orderByAsc("msg_id")
                .limit(rows)
                .selectList("*", MessageModel.class);
    }

    public void setMessageRouteState(MessageModel msg, boolean dist_routed) {
        try {
            _db.table("water_msg_message")
                    .set("dist_routed", dist_routed)
                    .whereEq("msg_id", msg.msg_id)
                    .update();

            msg.dist_routed = dist_routed;
        } catch (Exception ex) {
            //ex.printStackTrace();

            _logMsg.error("", msg.msg_id + "", "setMessageRouteState", msg.msg_id + "", ex);
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
                    .whereEq("msg_id", msg.msg_id).andIn("state", Arrays.asList(0, 1))
                    .update();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            _logMsg.error(msg.topic_name, msg.msg_id + "", "setMessageState", "", ex);

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
                    .setInc("dist_count", 1)
                    .whereEq("msg_id", msg.msg_id).andIn("state", Arrays.asList(0, 1))
                    .update();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();

            _logMsg.error(msg.topic_name, msg.msg_id + "", "setMessageRepet", msg.msg_id + "", ex);

            return false;
        }
    }


    //添加派发任务
    public void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws SQLException {


        boolean isExists = _db.table("water_msg_distribution")
                .whereEq("msg_id", msg.msg_id)
                .andEq("subscriber_id", subs.subscriber_id)
                .hint("/*TDDL:MASTER*/")
                .selectExists();

        if (isExists == false) {
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
        }
    }

    //根据消息获取派发任务
    public List<DistributionModel> getDistributionListByMsg(long msg_id) throws SQLException {
        return _db.table("water_msg_distribution")
                .whereEq("msg_id", msg_id).andIn("state", Arrays.asList(0, 1))
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
                    .whereEq("msg_id", msg.msg_id).andEq("subscriber_id", dist.subscriber_id).andNeq("state", 2)
                    .update();

            return true;
        } catch (Throwable ex) {

            _logMsg.error(msg.topic_name, msg.msg_id + "", "setDistributionState", "", ex);

            return false;
        }
    }

    /////////
    // for admin

    public MessageModel getMessageByKey(String msg_key) throws SQLException {
        if (TextUtils.isEmpty(msg_key)) {
            return new MessageModel();
        }

        return _db.table("water_msg_message")
                .build((tb) -> {
                    if (IdBuilder.isNumeric(msg_key)) {
                        tb.whereEq("msg_id", Long.parseLong(msg_key));
                    } else {
                        tb.whereEq("msg_key", msg_key);
                    }
                })
                .selectItem("*", MessageModel.class);
    }


    public MessageModel getMessageById(long msg_id) throws SQLException {
        return _db.table("water_msg_message")
                .whereEq("msg_id", msg_id)
                .limit(1)
                .selectItem("*", MessageModel.class);
    }

    //获取消息列表
    public List<MessageModel> getMessageList(int dist_count, int topic_id) throws SQLException {
        List<MessageModel> list = new ArrayList<>();

        if (dist_count == 0 && topic_id == 0) {
            return list;
        } else {
            return _db.table("water_msg_message").build((tb) -> {
                tb.whereEq("state", 0);
                if (dist_count > 0) {
                    tb.andGte("dist_count", dist_count);
                } else {
                    tb.andEq("topic_id", topic_id);
                }
            }).orderByAsc("msg_id").limit(50)
                    .selectList("*", MessageModel.class);
        }
    }

    public List<MessageModel> getMessageList(int _m, String key) throws SQLException {
        DbTableQuery qr = _db.table("water_msg_message");

        if (_m == 0) {
            qr.whereEq("state", 0).andGte("dist_count", 2);
        } else if (_m == 1) {
            qr.whereEq("state", 0);
        } else if (_m == 2) {
            qr.whereEq("state", 1);
        } else if (_m == 3) {
            qr.whereIn("state", Arrays.asList(2,3));
        } else {
            qr.whereLt("state", 0);
        }

        if (key != null) {
            key = key.trim();

            if (key.startsWith("*")) {
                qr.andEq("trace_id", key.substring(1).trim());
            } else if (key.startsWith("@")) {
                qr.andLk("tags", key.substring(1).trim() + "%");
            } else {
                if (StringUtils.isNumeric(key)) {
                    qr.andEq("msg_id", Integer.parseInt(key));
                } else {
                    qr.andEq("topic_name", key);
                }
            }
        }

        return qr.orderByDesc("msg_id").limit(50)
                .selectList("*", MessageModel.class);
    }


    //派发功能
    public boolean setMessageAsPending(List<Object> ids) throws SQLException {
        return _db.table("water_msg_message")
                .whereIn("msg_id", ids).andNeq("state", 2)
                .set("state", 0)
                .set("dist_nexttime", 0)
                .update() > 0;

    }


    //肖除一个状态的消息 //不包括0,2
//    public int deleteMsg(int state) throws SQLException {
//        if (state == 0 || state == 1) {
//            return -1;
//        }
//
//        int date = Datetime.Now().addDay(-3).getDate();
//
//        _db.table("#d")
//                .from("water_msg_distribution d,water_msg_message m")
//                .where(" d.msg_id = m.msg_id AND m.log_date<=? and m.state=?", date, state)
//                .delete();
//
//
//        return _db.table("water_msg_message")
//                .where("log_date<=? AND state=?", date, state)
//                .delete();
//    }

    //获得异常消息的dist_id和subscriber_id。
    public List<DistributionModel> getDistributionListByMsgIds(List<Object> ids) throws SQLException {
        return _db.table("water_msg_distribution")
                .whereIn("msg_id", ids)
                .selectList("dist_id,subscriber_id", DistributionModel.class);
    }


    //更新distribution中url
    public boolean setDistributionReceiveUrl(long dist_id, String receive_url) throws SQLException {
        return _db.table("water_msg_distribution")
                .whereEq("dist_id", dist_id)
                .set("receive_url", receive_url)
                .update() > 0;
    }

    //取消派发
    public boolean setMessageAsCancel(List<Object> ids) throws SQLException {
        return _db.table("water_msg_message")
                .whereIn("msg_id", ids)
                .set("state", -1)
                .update() > 0;
    }

    @Override
    public void clear(int keep_days) {

    }

    @Override
    public long reset(int seconds) throws SQLException {
        if (seconds < 10) {
            seconds = 30;
        }

        long currTime = System.currentTimeMillis();
        long timeOuts = 1000 * seconds; //30s
        long refTime = currTime - timeOuts;

        if (_db.table("water_msg_message").whereEq("state", 1).andLt("dist_nexttime", refTime).selectExists()) {
            return _db.table("water_msg_message")
                    .set("state", 0)
                    .whereEq("state", 1).andLt("dist_nexttime", refTime)
                    .update();
        } else {
            return 0;
        }
    }
}
