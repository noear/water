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
    //for api
    boolean hasMessage(String msg_key) throws SQLException;
    void cancelMessage(String msg_key) throws SQLException;
    void succeedMessage(String msg_key) throws SQLException;
    void cancelMsgDistribution(String msg_key, String subscriber_key) throws SQLException;
    void succeedMsgDistribution(String msg_key, String subscriber_key) throws SQLException;
    long addMessage(int topic_id, String topic_name, String content) throws Exception;
    long addMessage(String msg_key, String trace_id, String tags, int topic_id,String topic_name, String content, Date plan_time) throws Exception;


    //for sev
    List<Long> getMessageList(int rows, long dist_nexttime) throws SQLException;
    MessageModel getMessageOfPending(long msg_id) throws SQLException;
    void setMessageRouteState(MessageModel msg, boolean dist_routed);
    boolean setMessageState(MessageModel msg, MessageState state);
    boolean setMessageState(MessageModel msg, MessageState state, long dist_nexttime);
    boolean setMessageRepet(MessageModel msg, MessageState state);

    void addDistributionNoLock(MessageModel msg, SubscriberModel subs) throws SQLException;
    List<DistributionModel> getDistributionListByMsg(long msg_id) throws SQLException;
    boolean setDistributionState(MessageModel msg, DistributionModel dist, MessageState state);


    //for admin
    MessageModel getMessageByKey(String msg_key) throws SQLException;
    List<MessageModel> getMessageList(int dist_count, int topic_id) throws SQLException;
    List<MessageModel> getMessageList(int _m, String key) throws SQLException;
    boolean msgDistribute(List<Object> ids) throws SQLException;
    MessageModel getMessageById(long msg_id) throws SQLException;
    int deleteMsg(int state) throws SQLException;
    List<DistributionModel> repairSubs1(List<Object> ids) throws SQLException;
    boolean repairSubs3(long dist_id, String receive_url) throws SQLException;
    boolean cancelSend(List<Object> ids) throws SQLException;
}
