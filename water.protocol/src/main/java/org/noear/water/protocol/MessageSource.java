package org.noear.water.protocol;

import org.noear.water.protocol.model.message.DistributionModel;
import org.noear.water.protocol.model.message.MessageModel;
import org.noear.water.protocol.model.message.MessageState;
import org.noear.water.protocol.model.message.SubscriberModel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author noear 2021/2/1 created
 */
public interface MessageSource {
    //
    //for api
    //
    boolean hasMessage(String msg_key) throws Exception;
    void setMessageAsCancel(String msg_key) throws Exception;
    void setMessageAsSucceed(String msg_key) throws Exception;
    void setDistributionAsCancel(String msg_key, String subscriber_key) throws Exception;
    void setDistributionAsSucceed(String msg_key, String subscriber_key) throws Exception;
    long addMessage(int topic_id, String topic_name, String content) throws Exception;
    long addMessage(String msg_key, String trace_id, String tags, int topic_id,String topic_name, String content, Date plan_time) throws Exception;


    //
    //for sev
    //
    List<Long> getMessageListOfPending(int rows, long dist_nexttime) throws Exception;
    MessageModel getMessageOfPending(long msg_id) throws Exception;
    void setMessageRouteState(MessageModel msg, boolean dist_routed);
    boolean setMessageState(MessageModel msg, MessageState state);
    boolean setMessageState(MessageModel msg, MessageState state, long dist_nexttime);
    boolean setMessageRepet(MessageModel msg, MessageState state);

    void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws Exception;
    List<DistributionModel> getDistributionListByMsg(long msg_id) throws Exception;
    boolean setDistributionState(MessageModel msg, DistributionModel dist, MessageState state);


    //
    //for admin
    //
    MessageModel getMessageByKey(String msg_key) throws Exception;
    MessageModel getMessageById(long msg_id) throws Exception;
    List<MessageModel> getMessageList(int dist_count, int topic_id) throws Exception;
    List<MessageModel> getMessageList(int _m, String key) throws Exception;
//    int deleteMsg(int state) throws Exception;
    /**
     * 获取派发记录
     * */
    List<DistributionModel> getDistributionListByMsgIds(List<Object> ids) throws Exception;
    /**
     * 修复派发的订阅地址
     * */
    boolean setDistributionReceiveUrl(long dist_id, String receive_url) throws Exception;

    /**
     * 设置消息为预备状态
     * */
    boolean setMessageAsPending(List<Object> ids) throws Exception;
    /**
     * 设置消息为取消状态
     * */
    boolean setMessageAsCancel(List<Object> ids) throws Exception;


    void clear(int keep_days);

    long reset(int seconds) throws Exception;
}
