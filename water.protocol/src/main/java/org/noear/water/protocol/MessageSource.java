package org.noear.water.protocol;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author noear 2021/2/1 created
 */
public interface MessageSource {
    boolean hasMessage(String msg_key) throws SQLException;
    void cancelMessage(String msg_key) throws SQLException;
    void succeedMessage(String msg_key) throws SQLException;
    void cancelMsgDistribution(String msg_key, String subscriber_key) throws SQLException;
    void succeedMsgDistribution(String msg_key, String subscriber_key) throws SQLException;
    long addMessage(String topic, String content) throws Exception;
    long addMessage(String msg_key, String trace_id, String tags, String topic, String content, Date plan_time) throws Exception;
}
