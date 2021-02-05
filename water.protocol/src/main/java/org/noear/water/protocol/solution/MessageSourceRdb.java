package org.noear.water.protocol.solution;

import org.noear.water.protocol.IdBuilder;
import org.noear.water.protocol.MessageSource;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.DisttimeUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author noear 2021/2/3 created
 */
public class MessageSourceRdb implements MessageSource {
    DbContext _db;
    ICacheServiceEx _cache;
    IdBuilder _idBuilder;

    public MessageSourceRdb(DbContext db, ICacheServiceEx cache){
        _db = db;
        _cache = cache;
    }


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

        _db.table("water_msg_message").usingExpr(true)
                .set("msg_id", msg_id)
                .set("msg_key", msg_key)
                .setDf("tags", tags, "")
                .setIf(TextUtils.isNotEmpty(trace_id), "trace_id", trace_id)
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

        if(plan_time != null){
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
}
