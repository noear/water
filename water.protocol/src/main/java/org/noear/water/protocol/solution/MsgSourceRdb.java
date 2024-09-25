package org.noear.water.protocol.solution;

import org.noear.solon.Utils;
import org.noear.solon.core.util.ResourceUtil;
import org.noear.water.protocol.MsgSource;
import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.protocol.model.message.SubscriberModel;
import org.noear.water.protocol.utils.SnowflakeUtils;
import org.noear.water.utils.*;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;
import org.noear.wood.cache.ICacheServiceEx;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/2/3 created
 *
 * 消息状态（-2无派发对象 ; -1:忽略；0:未处理；1处理中；2已完成；3派发超次数）
 */
public class MsgSourceRdb implements MsgSource {
    DbContext _db;
    ICacheServiceEx _cache;
    Logger _logMsg;

    public MsgSourceRdb(DbContext db, ICacheServiceEx cache, Logger log) {
        _db = db;
        _cache = cache;
        _logMsg = log;
    }

    /////////
    //for waterapi

    //取消消息（key）
    public void setMessageAsCancel(String msg_key) throws SQLException {
        Datetime datetime = Datetime.Now();

        _db.table("water_msg_message")
                .set("state", -1)
                .set("last_date", datetime.getDate())
                .set("last_fulltime", datetime.getTicks())
                .whereEq("msg_key", msg_key)
                .update();

        _db.table("water_msg_distribution")
                .set("msg_state", -1)
                .whereEq("msg_key", msg_key)
                .update();
    }

    //消费消息（key）（设为成功）
    public void setMessageAsSucceed(String msg_key) throws SQLException {
        Datetime datetime = Datetime.Now();

        _db.table("water_msg_message")
                .set("state", 2)
                .set("last_date", datetime.getDate())
                .set("last_fulltime", datetime.getTicks())
                .whereEq("msg_key", msg_key)
                .update();

        _db.table("water_msg_distribution")
                .set("msg_state", 2)
                .whereEq("msg_key", msg_key)
                .update();
    }

    //取消消息派发（key+subscriber_key）
    public void setDistributionAsCancel(String msg_key, String subscriber_key) throws SQLException {

        _db.table("water_msg_distribution")
                .set("state", -1)
                .whereEq("msg_key", msg_key).andEq("subscriber_key", subscriber_key)
                .update();
    }


    //消费消息派发（key+subscriber_key）（设为成功）
    public void setDistributionAsSucceed(String msg_key, String subscriber_key) throws SQLException {
        _db.table("water_msg_distribution")
                .set("state", 2)
                .whereEq("msg_key", msg_key).andEq("subscriber_key", subscriber_key)
                .update();
    }

    @Override
    public long addMessage( String topic_name, String content) throws Exception {
        return addMessage(null, null, null, topic_name, content, null, false);
    }

    //添加消息
    @Override
    public long addMessage(String msg_key, String trace_id, String tags,  String topic_name, String content, Date plan_time, boolean autoDelay) throws Exception {
        long msg_id = SnowflakeUtils.genId();//ProtocolHub.idBuilder.getMsgId();

        if (Utils.isEmpty(msg_key)) {
            msg_key = Utils.guid();
        }

        if (Utils.isEmpty(trace_id)) {
            trace_id = Utils.guid();
        }

        if (tags == null) {
            tags = "";
        }

        Datetime datetime = new Datetime();
        int date = datetime.getDate();

        long dist_nexttime = 0;
        if (plan_time != null) {
            dist_nexttime = DisttimeUtils.distTime(plan_time);
        } else if (autoDelay) {
            dist_nexttime = DisttimeUtils.nextTime(0);
        }

        _db.table("water_msg_message")
                .set("msg_id", msg_id)
                .set("msg_key", msg_key)
                .set("tags", tags)
                .set("trace_id", trace_id)
                .set("topic_name", topic_name)
                .set("content", content)
                .set("plan_time", plan_time)
                .set("log_date", date)
                .set("log_fulltime", datetime.getTicks())
                .set("last_date", date)
                .set("last_fulltime", datetime.getTicks())
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
            //不需要记录最后时间
            _db.table("water_msg_message")
                    .set("dist_routed", dist_routed)
                    .whereEq("msg_id", msg.msg_id)
                    .update();

            msg.dist_routed = dist_routed;
        } catch (Exception ex) {
            //ex.printStackTrace();

            MDC.put("tag1", String.valueOf(msg.msg_id));
            MDC.put("tag2", "setMessageRouteState");
            _logMsg.error("{}", ex);
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
            Datetime datetime = Datetime.Now();

            _db.table("water_msg_message")
                    .set("state", state.code)
                    .set("last_date", datetime.getDate())
                    .set("last_fulltime", datetime.getTicks())
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

            _db.table("water_msg_distribution")
                    .set("msg_state", state.code)
                    .whereEq("msg_id", msg.msg_id)
                    .update();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            MDC.put("tag0", msg.topic_name);
            MDC.put("tag1", String.valueOf(msg.msg_id));
            MDC.put("tag2", "setMessageState");

            _logMsg.error("{}", ex);

            return false;
        }
    }

    //设置消息重试状态（过几秒后再派发）
    public boolean setMessageRepet(MessageModel msg, MessageState state) {
        try {
            msg.dist_count += 1;

            long ntime = DisttimeUtils.nextTime(msg.dist_count);
            Datetime datetime = Datetime.Now();

            _db.table("water_msg_message").usingExpr(true)
                    .set("state", state.code)
                    .set("last_date", datetime.getDate())
                    .set("last_fulltime", datetime.getTicks())
                    .set("dist_nexttime", ntime)
                    .set("dist_count", msg.dist_count)
                    .whereEq("msg_id", msg.msg_id).andIn("state", Arrays.asList(0, 1))
                    .update();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();

            MDC.put("tag0", msg.topic_name);
            MDC.put("tag1", String.valueOf(msg.msg_id));
            MDC.put("tag2", "setMessageRepet");

            _logMsg.error("{}", ex);

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
            Datetime datetime = new Datetime();
            long dist_id =  SnowflakeUtils.genId();

            _db.table("water_msg_distribution").usingExpr(true)
                    .set("dist_id", dist_id)
                    .set("msg_id", msg.msg_id)
                    .set("msg_key", msg.msg_key)
                    .set("subscriber_id", subs.subscriber_id)
                    .set("subscriber_key", subs.subscriber_key)
                    .set("alarm_mobile", subs.alarm_mobile)
                    .set("alarm_sign", subs.alarm_sign)
                    .set("receive_url", subs.receive_url)
                    .set("receive_key", subs.receive_key)
                    .set("receive_way", subs.receive_way)
                    .set("log_date", datetime.getDate())
                    .set("log_fulltime", datetime.getTicks())
                    .insert();
        }
    }

    //根据消息获取派发任务
    public List<DistributionModel> getDistributionListByMsg(long msg_id) throws SQLException {
        //不能有缓存
        return _db.table("water_msg_distribution")
                .whereEq("msg_id", msg_id).andIn("state", Arrays.asList(0, 1))
                .hint("/*TDDL:MASTER*/")
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

            MDC.put("tag0", msg.topic_name);
            MDC.put("tag1", String.valueOf(msg.msg_id));
            MDC.put("tag2", "setDistributionState");

            _logMsg.error("{}", ex);

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
                    if (TextUtils.isNumeric(msg_key)) {
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


    public List<MessageModel> getMessageList(int _m, String key) throws SQLException {
        DbTableQuery qr = _db.table("water_msg_message");

        if (_m == 0) {
            qr.whereEq("state", MessageState.undefined.code).andGte("dist_count", 2);
        } else if (_m == 1) {
            qr.whereEq("state", MessageState.undefined.code);
        } else if (_m == 2) {
            qr.whereEq("state", MessageState.processed.code);
        } else if (_m == 3) {
            qr.whereIn("state", Arrays.asList(
                    MessageState.completed.code,
                    MessageState.excessive.code));
        } else {
            qr.whereLt("state", 0);
        }

        if (TextUtils.isNotEmpty(key)) {
            if (key.startsWith("*")) {
                qr.andEq("trace_id", key.substring(1).trim());
            } else if (key.startsWith("@")) {
                qr.andEq("tags", key.substring(1).trim());
            } else {
                if (TextUtils.isNumeric(key)) {
                    qr.andEq("msg_id", Long.parseLong(key));
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
        Datetime datetime = Datetime.Now();

        return _db.table("water_msg_message")
                .whereIn("msg_id", ids).andNeq("state", 2)
                .set("state", 0)
                .set("last_date", datetime.getDate())
                .set("last_fulltime", datetime.getTicks())
                .set("dist_nexttime", 0)
                .update() > 0;

    }

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
        Datetime datetime = Datetime.Now();

        return _db.table("water_msg_message")
                .whereIn("msg_id", ids)
                .set("state", -1)
                .set("last_date", datetime.getDate())
                .set("last_fulltime", datetime.getTicks())
                .update() > 0;
    }

    @Override
    public long getWarnCount() throws Exception {
        /**
         * select count(*) num
         * from `water_msg_message`
         * where state=0 and dist_count=0 and dist_nexttime=0
         * */
        return _db.table("water_msg_message")
                .whereEq("state", 0)
                .andEq("dist_count", 0)
                .andEq("dist_nexttime", 0)
                .selectCount();
    }

    @Override
    public void create() throws Exception {
        String sql = ResourceUtil.getResourceAsString("water/water_msg_distribution_rdb_tml.sql", "utf-8");
        if (TextUtils.isNotEmpty(sql)) {
            try {
                _db.exe(sql);
            } catch (SQLException e) {
                throw new RuntimeException("[water_msg_distribution] create failure: " + e.getLocalizedMessage());
            }
        }

        sql = ResourceUtil.getResourceAsString("water/water_msg_message_all_rdb_tml.sql", "utf-8");
        if (TextUtils.isNotEmpty(sql)) {
            try {
                _db.exe(sql);
            } catch (SQLException e) {
                throw new RuntimeException("[water_msg_message_all] create failure: " + e.getLocalizedMessage());
            }
        }

        sql = ResourceUtil.getResourceAsString("water/water_msg_message_rdb_tml.sql", "utf-8");
        if (TextUtils.isNotEmpty(sql)) {
            try {
                _db.exe(sql);
            } catch (SQLException e) {
                throw new RuntimeException("[water_msg_message] create failure: " + e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void clear(int lteDate) throws Exception {
        _db.table("water_msg_message").whereLte("last_date", lteDate).andEq("state", 2).delete();
        _db.table("water_msg_message").whereLte("last_date", lteDate).andEq("state", 3).delete();
        _db.table("water_msg_message").whereLte("last_date", lteDate).andEq("state", -1).delete();
        _db.table("water_msg_message").whereLte("last_date", lteDate).andEq("state", -2).delete();

        _db.table("water_msg_distribution").whereLte("log_date", lteDate).andEq("msg_state", 2).delete();
        _db.table("water_msg_distribution").whereLte("log_date", lteDate).andEq("msg_state", 3).delete();
        _db.table("water_msg_distribution").whereLte("log_date", lteDate).andEq("msg_state", -1).delete();
        _db.table("water_msg_distribution").whereLte("log_date", lteDate).andEq("msg_state", -2).delete();
    }

    @Override
    public long reset(int seconds) throws SQLException {
        if (seconds < 10) {
            seconds = 10;
        }

        long currTime = System.currentTimeMillis();
        long timeOuts = 1000 * seconds; //30s
        long refTime = currTime - timeOuts;

        if (_db.table("water_msg_message")
                .whereEq("state", 1)
                .andLt("dist_nexttime", refTime)
                .andLt("last_fulltime", refTime).selectExists()) {
            Datetime datetime = Datetime.Now();

            return _db.table("water_msg_message")
                    .set("state", 0)
                    .set("last_date", datetime.getDate())
                    .set("last_fulltime", datetime.getTicks())
                    .whereEq("state", 1)
                    .andLt("dist_nexttime", refTime)
                    .andLt("last_fulltime", refTime)
                    .update();
        } else {
            return 0;
        }
    }

    @Override
    public void persistence(int hotDate, int coldDate) throws Exception {
        //转移数据（长久保存） //根据创建时间转移
        //
        if (_db.table("water_msg_message_all").whereEq("log_date", hotDate).selectExists() == false) {
            _db.exe("INSERT INTO water_msg_message_all " +
                    "SELECT * FROM water_msg_message WHERE log_date = ?", hotDate);
        }

        //清理持久化
        _db.table("water_msg_message_all").whereLte("log_date", coldDate);
    }

    @Override
    public void close() throws IOException {
        _db.close();
    }
}
