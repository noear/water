package org.noear.water.client.dso;

import org.noear.snack.ONode;
import org.noear.water.client.model.ReceiveWay;
import org.noear.water.tools.AssertUtils;
import org.noear.water.tools.Datetime;
import org.noear.water.tools.IDUtil;
import org.noear.water.tools.StringUtils;

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
    public static ONode subscribe(String subscriber_key, String receiver_url, String receive_secret, String alarm_mobile, ReceiveWay receive_way, String... topics) throws Exception {
        return subscribe(subscriber_key, "", receiver_url, receive_secret, alarm_mobile, receive_way, topics);
    }

    /**
     * 订阅主题
     */
    public static ONode subscribe(String subscriber_key, String subscriber_note, String receiver_url, String receive_secret, String alarm_mobile, ReceiveWay receive_way, String... topics) throws Exception {
        Map<String, String> params = new HashMap<>();

        params.put("subscriber_key", subscriber_key);
        params.put("subscriber_note", subscriber_note);

        params.put("topic", String.join(",", topics));

        params.put("receive_url", receiver_url);
        params.put("receive_way", receive_way.code + "");
        params.put("receive_secret", receive_secret);

        params.put("alarm_mobile", alarm_mobile);

        String txt = WaterApi.post("/msg/subscribe/", params);

        return ONode.loadStr(txt);
    }

    /**
     * 取消订阅主题
     */
    public static ONode unsubscribe(String subscriber_key, String... topics) throws Exception {
        Map<String, String> params = new HashMap<>();

        params.put("subscriber_key", subscriber_key);

        params.put("topic", String.join(",", topics));

        String txt = WaterApi.post("/msg/unsubscribe/", params);

        return ONode.loadStr(txt);
    }

    /**
     * 发送消息
     */
    public static String messageSend(String topic, String message) throws Exception {
        return messageSend(topic,message,null,null,null);
    }

    /**
     * 发送消息
     *
     * @param planTime 计划派发时间
     */
    public static String messageSend(String topic, String message, Date planTime) throws Exception {
        return messageSend(topic,message,planTime,null,null);
    }

    /**
     * 发送消息
     *
     * @param planTime 计划派发时间
     * @param receive_url 直接接收地址（不需要认阅）
     * @param receive_cehck 直接接收检查
     */
    public static String messageSend(String topic, String message, Date planTime, String receive_url, String receive_cehck) throws Exception {
        AssertUtils.notEmpty(message,"message");

        String msg_key = IDUtil.guid();

        Map<String, String> params = new HashMap<>();
        params.put("key", msg_key);

        if (topic != null) {
            params.put("topic", topic);
        }

        if (message != null) {
            params.put("message", message);
        }

        if (receive_url != null) {
            params.put("receive_url", receive_url);
        }

        if (receive_cehck != null) {
            params.put("receive_cehck", receive_cehck);
        }

        if (planTime != null) {
            String planTime2 = new Datetime(planTime).toString("yyyy-MM-dd HH:mm:ss");
            params.put("plan_time", planTime2);
        }

        String rst = WaterApi.post("/msg/send/", params);
        ONode rst2 = ONode.load(rst);

        if (rst2.get("code").getInt() == 1) {
            return msg_key;
        } else {
            throw new Exception("消息发送失败:" + rst);
        }
    }

    /**
     * 取消消息
     */
    public static ONode messageCancel(String msg_key) throws Exception {
        return messageCancel(msg_key, null);
    }

    /**
     * 取消订阅者的消息
     *
     * @param subscriber_key 订阅者KEY
     */
    public static ONode messageCancel(String msg_key, String subscriber_key) throws Exception {
        Map<String, String> params = new HashMap<>();
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
    public static ONode messageSucceed(String msg_key) throws Exception {
        return messageSucceed(msg_key, null);
    }

    /**
     * 完成订阅者的消息
     *
     * @param subscriber_key 订阅者KEY
     */
    public static ONode messageSucceed(String msg_key, String subscriber_key) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key", msg_key);

        if (StringUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }


        String txt = WaterApi.post("/msg/succeed/", params);

        return ONode.loadStr(txt);
    }
}
