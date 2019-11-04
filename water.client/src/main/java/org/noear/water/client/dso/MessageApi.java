package org.noear.water.client.dso;

import org.noear.snack.ONode;
import org.noear.water.client.model.ReceiveWay;
import org.noear.water.client.utils.Datetime;
import org.noear.water.client.utils.IDUtil;
import org.noear.water.client.utils.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息接口
 * */
public class MessageApi {
    //0:http异步等待
    //1:http同步等待
    //2:http异步不等待

    /**
     * 订阅主题
     */
    public static ONode subscribeTopic(String subscriber_key, String receiver_url, String access_key, String alarm_mobile, ReceiveWay receive_way, String... topics) throws Exception {
        return subscribeTopic(subscriber_key, "", receiver_url, access_key, alarm_mobile, receive_way, topics);
    }

    /**
     * 订阅主题
     */
    public static ONode subscribeTopic(String subscriber_key, String subscriber_note, String receiver_url, String access_key, String alarm_mobile, ReceiveWay receive_way, String... topics) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", subscriber_key);
        params.put("note", subscriber_note);
        params.put("topic", String.join(",", topics));
        params.put("receiver_url", receiver_url);
        params.put("access_key", access_key);
        params.put("alarm_mobile", alarm_mobile);
        params.put("receive_way", receive_way.code + "");

        String txt = WaterApi.post("/msg/subscribe/", params);

        return ONode.loadStr(txt);
    }

    /**
     * 取消订阅主题
     */
    public static ONode unsubscribeTopic(String subscriber_key, String... topics) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", subscriber_key);
        params.put("topic", String.join(",", topics));

        String txt = WaterApi.post("/msg/unsubscribe/", params);

        return ONode.loadStr(txt);
    }

    /**
     * 发送消息
     */
    public static ONode sendMessage(String topic, String message) throws Exception {
        return sendMessage(IDUtil.guid(), topic, message, null);
    }

    /**
     * 发送消息
     */
    public static ONode sendMessage(String msg_key, String topic, String message) throws Exception {
        return sendMessage(msg_key, topic, message, null);
    }

    /**
     * 发送消息
     */
    public static ONode sendMessage(String msg_key, String topic, String message, Date planTime) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", msg_key);
        params.put("topic", topic);
        params.put("message", message);


        if (planTime != null) {
            String planTime2 = new Datetime(planTime).toString("yyyy-MM-dd HH:mm:ss");
            params.put("plan_time", planTime2);
        }

        String txt = WaterApi.post("/msg/send/", params);

        ONode data = ONode.loadStr(txt);
        if (data.get("code").getInt() == 1) {
            return data;
        } else {
            throw new Exception("消息发送失败:" + data.toJson());
        }
    }

    public static ONode sendMessageCall(String message, String receiver_url, String receiver_cehck) throws Exception {
        return sendMessageCall(IDUtil.guid(), message, null, receiver_url, receiver_cehck);
    }

    public static ONode sendMessageCall(String msg_key, String message, Date planTime, String receiver_url, String receiver_cehck) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", msg_key);
        params.put("message", message);
        params.put("receiver_url", receiver_url);
        params.put("receiver_cehck", receiver_cehck);


        if (planTime != null) {
            String planTime2 = new Datetime(planTime).toString("yyyy-MM-dd HH:mm:ss");
            params.put("plan_time", planTime2);
        }

        String txt = WaterApi.post("/msg/send/", params);

        ONode data = ONode.loadStr(txt);
        if (data.get("code").getInt() == 1) {
            return data;
        } else {
            throw new Exception("消息发送失败:" + data.toJson());
        }
    }

    /**
     * 取消消息
     */
    public static ONode cancelMessage(String msg_key) throws Exception {
        return cancelMessage(msg_key, null);
    }

    /**
     * 取消XXX订阅者的消息
     */
    public static ONode cancelMessage(String msg_key, String subscriber_key) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", msg_key);

        if (StringUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }

        String txt = WaterApi.post("/msg/cancel/", params);

        return ONode.loadStr(txt);
    }

    /**
     * 完成消息
     */
    public static ONode succeedMessage(String msg_key) throws Exception {
        return succeedMessage(msg_key, null);
    }

    /**
     * 完成XXX订阅者的消息
     */
    public static ONode succeedMessage(String msg_key, String subscriber_key) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", msg_key);

        if (StringUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }


        String txt = WaterApi.post("/msg/succeed/", params);

        return ONode.loadStr(txt);
    }
}
