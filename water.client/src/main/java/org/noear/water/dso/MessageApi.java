package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.model.MessageM;
import org.noear.water.utils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息服务接口
 * */
public class MessageApi {

    /**
     * 订阅
     */
    public ONode subscribeTopic(String subscriber_key, String receiver_url, String access_key, String alarm_mobile, int receive_way, String... topics) throws Exception {
        return subscribeTopic(subscriber_key, "", receiver_url, access_key, alarm_mobile, receive_way, topics);
    }

    /**
     * 订阅
     *
     * @param subscriber_key  订阅者标识
     * @param subscriber_note 订阅者简介
     * @param receiver_url    接收地址
     * @param receive_way     接收方式 (0:http异步等待, 1:http同步等待, 2:http异步不等待)
     * @param access_key      接收访问密钥
     * @param alarm_mobile    报警手机号
     * @param topics          主题..
     */
    public ONode subscribeTopic(String subscriber_key, String subscriber_note, String receiver_url, String access_key, String alarm_mobile, int receive_way, String... topics) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key", subscriber_key);
        params.put("note", subscriber_note);
        params.put("topic", String.join(",", topics));
        params.put("receiver_url", receiver_url);
        params.put("access_key", access_key);
        params.put("alarm_mobile", alarm_mobile);
        params.put("receive_way", receive_way + "");

        String txt = CallSevUtil.post("/msg/subscribe/", params);

        System.out.println("MessageApi::/msg/subscribe/:" + txt);

        return ONode.loadStr(txt);
    }

    /**
     * 取消麻阅
     *
     * @param subscriber_key 订阅者标识
     * @param topics         主题..
     */
    public ONode unSubscribeTopic(String subscriber_key, String... topics) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key", subscriber_key);
        params.put("topic", String.join(",", topics));

        String txt = CallSevUtil.post("/msg/unsubscribe/", params);

        System.out.println("MessageApi::/msg/unsubscribe/:" + txt);

        return ONode.loadStr(txt);
    }

    /**
     * 发送消息
     */
    public ONode sendMessage(String topic, String message) throws Exception {
        return sendMessage(null, topic, message, null);
    }


    /**
     * 发送消息
     */
    public ONode sendMessage(String msg_key, String topic, String message) throws Exception {
        return sendMessage(msg_key, topic, message, null);
    }


    /**
     * 发送消息
     *
     * @param msg_key  消息标识（用于建立本地关键）
     * @param topic    主题
     * @param message  消息内容
     * @param planTime 计划通知时间
     */
    public ONode sendMessage(String msg_key, String topic, String message, Date planTime) throws Exception {
        return sendMessageAndTags(msg_key, topic, message, planTime, null);
    }


    public ONode sendMessageAndTags(String topic, String message, String tags) throws Exception {
        return sendMessageAndTags(null, topic, message, tags);
    }

    public ONode sendMessageAndTags(String msg_key, String topic, String message, String tags) throws Exception {
        return sendMessageAndTags(msg_key, topic, message, null, tags);
    }

    /**
     * 发送消息
     *
     * @param msg_key  消息标识（用于建立本地关键）
     * @param tags           标签
     * @param topic    主题
     * @param message  消息内容
     * @param planTime 计划通知时间
     */
    public ONode sendMessageAndTags(String msg_key, String topic, String message, Date planTime, String tags) throws Exception {
        if (TextUtils.isEmpty(msg_key)) {
            msg_key = IDUtils.guid();
        }

        if (tags == null) {
            tags = "";
        }

        Map<String, String> params = new HashMap<>();
        params.put("key", msg_key);
        params.put("tags", tags);
        params.put("topic", topic);
        params.put("message", message);


        if (planTime != null) {
            String planTime2 = new Datetime(planTime).toString("yyyy-MM-dd HH:mm:ss");
            params.put("plan_time", planTime2);
        }

        String txt = CallSevUtil.post("/msg/send/", params);

        System.out.println("MessageApi::/msg/send/:" + txt);

        ONode data = ONode.loadStr(txt);
        if (data.get("code").getInt() == 1) {
            return data;
        } else {
            throw new Exception("消息发送失败:" + data.toJson());
        }
    }

    /**
     * 发送消息回调
     */
    public ONode sendMessageCallback(String message, String receiver_url, String receiver_cehck) throws Exception {
        return sendMessageCallback(null, message, null, receiver_url, receiver_cehck);
    }

    public ONode sendMessageCallback(String msg_key, String message, Date planTime, String receiver_url, String receiver_cehck) throws Exception{
        return sendMessageAndTagsCallback(msg_key, message, planTime, receiver_url,receiver_cehck, null);
    }

    public ONode sendMessageAndTagsCallback(String message, String receiver_url, String receiver_cehck, String tags) throws Exception {
        return sendMessageAndTagsCallback(null, message, null, receiver_url, receiver_cehck, tags);
    }

    /**
     * 发送消息回调
     *
     * @param msg_key        消息标识
     * @param tags           标签
     * @param message        消息
     * @param receiver_url   接收地址
     * @param receiver_cehck 接收检测
     * @param planTime       计划发送时间
     */
    public ONode sendMessageAndTagsCallback(String msg_key, String message, Date planTime, String receiver_url, String receiver_cehck, String tags) throws Exception {
        if (TextUtils.isEmpty(msg_key)) {
            msg_key = IDUtils.guid();
        }

        if (tags == null) {
            tags = "";
        }

        Map<String, String> params = new HashMap<>();
        params.put("key", msg_key);
        params.put("tags", tags);
        params.put("message", message);
        params.put("receiver_url", receiver_url);
        params.put("receiver_cehck", receiver_cehck);


        if (planTime != null) {
            String planTime2 = new Datetime(planTime).toString("yyyy-MM-dd HH:mm:ss");
            params.put("plan_time", planTime2);
        }

        String txt = CallSevUtil.post("/msg/send/", params);

        System.out.println("MessageApi::/msg/send/:" + txt);

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
    public ONode cancelMessage(String msg_key) throws Exception {
        return cancelMessage(msg_key, null);
    }

    /**
     * 取消XXX订阅者的消息
     */
    public ONode cancelMessage(String msg_key, String subscriber_key) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key", msg_key);

        if (TextUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }

        String txt = CallSevUtil.post("/msg/cancel/", params);

        System.out.println("MessageApi::/msg/cancel/:" + txt);

        return ONode.loadStr(txt);
    }

    /**
     * 完成消息（设为成功）
     */
    public ONode succeedMessage(String msg_key) throws Exception {
        return succeedMessage(msg_key, null);
    }

    /**
     * 完成XXX订阅者的消息
     */
    public ONode succeedMessage(String msg_key, String subscriber_key) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key", msg_key);

        if (TextUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }


        String txt = CallSevUtil.post("/msg/succeed/", params);

        System.out.println("MessageApi::/msg/succeed/:" + txt);

        return ONode.loadStr(txt);
    }

    /**
     * 检测消息签名
     */
    public boolean checkMessage(MessageM msg, String access_key) {

        if (msg.id < 1) {
            return false;
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.id).append("#");
        sb.append(msg.key).append("#");
        sb.append(msg.topic).append("#");
        sb.append(msg.message).append("#");
        sb.append(access_key);

        String sgin_slf = EncryptUtils.md5(sb.toString());

        return sgin_slf.equals(msg.sgin);
    }
}
