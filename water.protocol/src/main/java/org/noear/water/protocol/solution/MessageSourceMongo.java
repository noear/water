package org.noear.water.protocol.solution;

import com.mongodb.client.model.IndexOptions;
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
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.mongo.MgContext;
import org.noear.weed.mongo.MgTableQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/2/5 created
 */
public class MessageSourceMongo implements MessageSource {
    MgContext _db;
    ICacheServiceEx _cache;
    Logger _logMsg;

    static final String COLL = "water_msg_message";

    public MessageSourceMongo(MgContext db, ICacheServiceEx cache, Logger log) {
        _db = db;
        _cache = cache;
        _logMsg = log;
    }

    /////////
    //for waterapi

    //检查是否已有消息（key）
    public boolean hasMessage(String msg_key) throws Exception {
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
    public void setMessageAsCancel(String msg_key) throws Exception {
        _db.table("water_msg_message")
                .set("state", -1)
                .whereEq("msg_key", msg_key)
                .update();

        _db.table("water_msg_distribution")
                .set("msg_state", -1)
                .whereEq("msg_key", msg_key)
                .update();
    }

    //消费消息（key）（设为成功）
    public void setMessageAsSucceed(String msg_key) throws Exception {
        _db.table("water_msg_message")
                .set("state", 2)
                .whereEq("msg_key", msg_key)
                .update();

        _db.table("water_msg_distribution")
                .set("msg_state", 2)
                .whereEq("msg_key", msg_key)
                .update();
    }

    //取消消息派发（key+subscriber_key）
    public void setDistributionAsCancel(String msg_key, String subscriber_key) throws Exception {

        _db.table("water_msg_distribution").set("state", -1)
                .whereEq("msg_key", msg_key).andEq("subscriber_key", subscriber_key)
                .update();
    }


    //消费消息派发（key+subscriber_key）（设为成功）
    public void setDistributionAsSucceed(String msg_key, String subscriber_key) throws Exception {
        _db.table("water_msg_distribution").set("state", 2)
                .whereEq("msg_key", msg_key).andEq("subscriber_key", subscriber_key)
                .update();
    }

    public long addMessage(int topic_id, String topic_name, String content) throws Exception {
        return addMessage(null, null, null, topic_id, topic_name, content, null);
    }

    //添加消息
    public long addMessage(String msg_key, String trace_id, String tags, int topic_id,String topic_name, String content, Date plan_time) throws Exception {
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

        Datetime datetime = new Datetime();

        long dist_nexttime = 0;
        if (plan_time != null) {
            dist_nexttime = DisttimeUtils.distTime(plan_time);
        }

        _db.table("water_msg_message")
                .set("_id", msg_id)
                .set("msg_id", msg_id)
                .set("msg_key", msg_key)
                .set("trace_id", trace_id)
                .set("tags", tags)
                .set("topic_id", topic_id)
                .set("topic_name", topic_name)
                .set("content", content)
                .set("receive_url", "")
                .set("receive_check", "")
                .set("dist_routed", false)
                .set("dist_count", 0)
                .set("dist_nexttime", 0L)
                .set("plan_time", plan_time)
                .set("state",0)
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
    public List<MessageModel> getMessageListOfPending(int rows, long dist_nexttime) throws Exception {
        return _db.table("water_msg_message")
                .whereLt("dist_nexttime", dist_nexttime).andEq("state", 0)
                .orderByAsc("msg_id")
                .limit(rows)
                .selectList(MessageModel.class);
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
                    .whereEq("msg_id",msg.msg_id).andGte("state", 0).andLte("state",1)
                    .update();

            _db.table("water_msg_distribution")
                    .set("msg_state", state.code)
                    .whereEq("msg_id", msg.msg_id)
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

            _db.table("water_msg_message")
                    .set("state", state.code)
                    .set("dist_nexttime", ntime)
                    .set("dist_count", msg.dist_count)
                    .whereEq("msg_id", msg.msg_id).andIn("state", Arrays.asList(0,1))
                    .update();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            _logMsg.error(msg.topic_name, msg.msg_id + "", "setMessageRepet", msg.msg_id + "", ex);

            return false;
        }
    }


    //添加派发任务
    public void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws Exception {

        boolean isExists = _db.table("water_msg_distribution")
                .whereEq("msg_id", msg.msg_id)
                .andEq("subscriber_id", subs.subscriber_id)
                .selectExists();

        if (isExists == false) {
            Datetime datetime = new Datetime();
            long dist_id = ProtocolHub.idBuilder.getId("water_msg_distribution");

            _db.table("water_msg_distribution")
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
                    .set("receive_check", "")
                    .set("duration", 0)
                    .set("state", 0)
                    .set("msg_state", 0)
                    .set("log_date", datetime.getDate())
                    .set("log_fulltime", datetime.getFulltime())
                    .whereEq("msg_id", msg.msg_id).andEq("subscriber_id", subs.subscriber_id)
                    .insert();
        }
    }

    //根据消息获取派发任务
    public List<DistributionModel> getDistributionListByMsg(long msg_id) throws Exception {
        return _db.table("water_msg_distribution")
                .whereEq("msg_id", msg_id).andIn("state", Arrays.asList(0, 1))
                .caching(_cache).usingCache(60)
                .selectList(DistributionModel.class);
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

    public  MessageModel getMessageByKey(String msg_key) throws Exception {
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
                .selectItem(MessageModel.class);
    }

    @Override
    public MessageModel getMessageById(long msg_id) throws Exception {
        return _db.table("water_msg_message")
                .whereEq("msg_id", msg_id)
                .limit(1)
                .selectItem(MessageModel.class);
    }

    //获取消息列表
    public  List<MessageModel> getMessageList(int dist_count, int topic_id) throws Exception {
        List<MessageModel> list = new ArrayList<>();

        if (dist_count == 0 && topic_id == 0) {
            return list;
        } else {
            return _db.table("water_msg_message").build((tb) -> {
                if (dist_count > 0) {
                    tb.whereGte("dist_count", dist_count);
                } else {
                    tb.whereEq("topic_id", topic_id);
                }

                tb.andEq("state", 0);
            }).orderByAsc("msg_id").limit(50)
                    .selectList(MessageModel.class);
        }
    }

    public  List<MessageModel> getMessageList(int _m, String key) throws Exception {
        MgTableQuery qr = _db.table("water_msg_message");

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

        if (key != null) {
            key = key.trim();

            if (key.startsWith("*")) {
                qr.andEq("trace_id", key.substring(1).trim());
            } else if (key.startsWith("@")) {
                qr.andLk("tags", "^" + key.substring(1).trim());
            } else {
                if (StringUtils.isNumeric(key)) {
                    qr.andEq("msg_id", Integer.parseInt(key));
                } else {
                    qr.andEq("topic_name", key);
                }
            }
        }

        return qr.orderByDesc("_id").limit(50)
                .selectList(MessageModel.class);
    }


    //派发功能
    public  boolean setMessageAsPending(List<Object> ids) throws Exception {
        return _db.table("water_msg_message")
                .whereIn("msg_id", ids).andNeq("state", 2)
                .set("state", 0)
                .set("dist_nexttime", 0)
                .update() > 0;

    }

    //获得异常消息的dist_id和subscriber_id。
    public  List<DistributionModel> getDistributionListByMsgIds(List<Object> ids) throws Exception {
        return _db.table("water_msg_distribution")
                .whereIn("msg_id", ids)
                .selectList( DistributionModel.class);
    }


    //更新distribution中url
    public  boolean setDistributionReceiveUrl(long dist_id, String receive_url) throws Exception {
        return _db.table("water_msg_distribution")
                .whereEq("dist_id", dist_id)
                .set("receive_url", receive_url)
                .update() > 0;
    }

    //取消派发
    public  boolean setMessageAsCancel(List<Object> ids) throws Exception {
        return _db.table("water_msg_message")
                .whereIn("msg_id", ids)
                .set("state", -1)
                .update() > 0;
    }

    private void initIndex(){
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.background(true);
        indexOptions.unique(true);

        _db.table("water_msg_message").orderByDesc("msg_key").createIndex(indexOptions);
        _db.table("water_msg_message").orderByDesc("msg_id").createIndex(indexOptions);
        _db.table("water_msg_message").orderByDesc("topic_id").createIndex(true);
        _db.table("water_msg_message").orderByDesc("state").createIndex(true);
        _db.table("water_msg_message").orderByDesc("dist_count").createIndex(true);
        _db.table("water_msg_message").orderByDesc("log_date").createIndex(true);
        _db.table("water_msg_message").orderByDesc("dist_nexttime").createIndex(true);
        _db.table("water_msg_message").orderByDesc("topic_name").createIndex(true);
        _db.table("water_msg_message").orderByDesc("tags").createIndex(true);
        _db.table("water_msg_message").orderByDesc("trace_id").createIndex(true);


        _db.table("water_msg_distribution").orderByDesc("dist_id").createIndex(indexOptions);
        _db.table("water_msg_distribution").orderByDesc("log_date").createIndex(true);
        _db.table("water_msg_distribution").orderByDesc("state").createIndex(true);
        _db.table("water_msg_distribution").orderByDesc("msg_id").createIndex(true);
        _db.table("water_msg_distribution").orderByDesc("msg_key").createIndex(true);
        _db.table("water_msg_distribution").orderByDesc("msg_state").createIndex(true);
    }

    @Override
    public void clear(int lteDate) throws Exception {
        _db.table("water_msg_message").whereLte("log_date",lteDate).andEq("state",2).delete();
        _db.table("water_msg_message").whereLte("log_date",lteDate).andEq("state",3).delete();
        _db.table("water_msg_message").whereLte("log_date",lteDate).andEq("state",-1).delete();
        _db.table("water_msg_message").whereLte("log_date",lteDate).andEq("state",-2).delete();

        _db.table("water_msg_distribution").whereLte("log_date",lteDate).andEq("msg_state",2).delete();
        _db.table("water_msg_distribution").whereLte("log_date",lteDate).andEq("msg_state",3).delete();
        _db.table("water_msg_distribution").whereLte("log_date",lteDate).andEq("msg_state",-1).delete();
        _db.table("water_msg_distribution").whereLte("log_date",lteDate).andEq("msg_state",-2).delete();
    }

    @Override
    public long reset(int seconds) throws SQLException {
        if (seconds < 10) {
            seconds = 30;
        }

        initIndex();

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
