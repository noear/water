package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.model.MessageM;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.IDUtils;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息服务接口
 *
 * @author noear
 * @since 2.0
 * */
public class MessageApi {
    protected final ApiCaller apiCaller;

    public MessageApi() {
        apiCaller = new ApiCaller(WaterAddress.getMsgApiUrl());
    }


    /**
     * 订阅
     *
     * @param subscriber_key  订阅者key
     * @param subscriber_name 订阅者name
     * @param subscriber_tag  订阅者tag
     * @param receive_url     接收地址
     * @param receive_way     接收方式 (0:http异步等待, 1:http同步等待, 2:http异步不等待)
     * @param receive_key     接收密钥
     * @param alarm_mobile    报警手机号
     * @param topics          主题（多个）
     */
    public boolean subscribeTopic(String broker, String subscriber_key, String subscriber_name, String subscriber_tag,  String receive_url, String receive_key, String alarm_mobile, int receive_way, boolean is_unstable, String[] topics) throws Exception {
        String topics_str = String.join(",", topics);

        Map<String, String> params = new HashMap<>();
        params.put("key", subscriber_key); //**此字段名将弃用。by 2020-09
        params.put("note", subscriber_name); //**此字段名将弃用。by 2020-09

        if (TextUtils.isNotEmpty(broker)) {
            params.put("broker", broker);
        }

        params.put("subscriber_key", subscriber_key);
        params.put("subscriber_name", subscriber_name);
        params.put("subscriber_tag", subscriber_tag);

        params.put("topic", topics_str);
        params.put("receiver_url", receive_url); //**此字段名将弃用。by 2020-09
        params.put("receive_url", receive_url); //支持：http:// 或 @service
        params.put("receive_way", receive_way + ""); //接收方式（0,1异步等待；2异步不等待,状态设为已完成；3异步不等,状态设为处理中）
        params.put("receive_key", receive_key);
        params.put("access_key", receive_key); //**此字段名将弃用。by 2021-02
        params.put("alarm_mobile", alarm_mobile);
        params.put("is_unstable", (is_unstable ? "1" : "0")); //用于兼容k8s的ip漂移

        String txt = apiCaller.post("/msg/subscribe/", params);

        int code = ONode.loadStr(txt).get("code").getInt();
        return code == 1 || code == 200;
    }

    /**
     * 取消麻阅
     *
     * @param subscriber_key 订阅者标识
     * @param topics         主题（多个）
     */
    public boolean unSubscribeTopic(String broker, String subscriber_key, String[] topics) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key", subscriber_key);//**此字段名将弃用。by 2020-09

        if (TextUtils.isNotEmpty(broker)) {
            params.put("broker", broker);
        }

        params.put("subscriber_key", subscriber_key);
        params.put("topic", String.join(",", topics));

        String txt = apiCaller.post("/msg/unsubscribe/", params);

        int code = ONode.loadStr(txt).get("code").getInt();
        return code == 1 || code == 200;
    }

    /**
     * 发送消息
     *
     * @param topic   消息主题
     * @param message 消息内容
     */
    public boolean sendMessage(String broker, String topic, String message) throws Exception {
        return sendMessage(broker, null, topic, message, null);
    }


    /**
     * 发送消息
     *
     * @param msg_key 消息key（用于建立本地关键）
     * @param topic   消息主题
     * @param message 消息内容
     */
    public boolean sendMessage(String broker, String msg_key, String topic, String message) throws Exception {
        return sendMessage(broker, msg_key, topic, message, null);
    }


    /**
     * 发送消息
     *
     * @param msg_key  消息标识（用于建立本地关键）
     * @param topic    主题
     * @param message  消息内容
     * @param planTime 计划通知时间
     */
    public boolean sendMessage(String broker, String msg_key, String topic, String message, Date planTime) throws Exception {
        return sendMessageAndTags(broker, msg_key, topic, message, planTime, null);
    }


    public boolean sendMessageAndTags(String broker, String topic, String message, String tags) throws Exception {
        return sendMessageAndTags(broker, null, topic, message, tags);
    }

    public boolean sendMessageAndTags(String broker, String msg_key, String topic, String message, String tags) throws Exception {
        return sendMessageAndTags(broker, msg_key, topic, message, null, tags);
    }

    /**
     * 发送消息
     *
     * @param msg_key  消息标识（用于建立本地关键）
     * @param tags     标签
     * @param topic    主题
     * @param message  消息内容
     * @param planTime 计划通知时间
     */
    public boolean sendMessageAndTags(String broker, String msg_key, String topic, String message, Date planTime, String tags) throws Exception {

        Map<String, String> params = new HashMap<>();

        if (TextUtils.isNotEmpty(broker)) {
            params.put("broker", broker);
        }

        params.put("topic", topic);
        params.put("message", message);

        if (msg_key != null) {
            params.put("key", msg_key);
        }

        if (tags != null) {
            params.put("tags", tags);
        }

        if (planTime != null) {
            params.put("plan_time", String.valueOf(planTime.getTime()));
        }

        String txt = apiCaller.post("/msg/send/", params);

        ONode data = ONode.loadStr(txt);
        int code = data.get("code").getInt();

        if (code == 1 || code == 200) {
            return true;
        } else {
            throw new RuntimeException("消息发送失败:" + data.toJson());
        }
    }

    /**
     * 发送消息回调
     */
    public boolean sendMessageCallback(String broker, String message, String receiver_url, String receiver_cehck) throws Exception {
        return sendMessageCallback(broker, null, message, null, receiver_url, receiver_cehck);
    }

    public boolean sendMessageCallback(String broker, String msg_key, String message, Date planTime, String receiver_url, String receiver_cehck) throws Exception {
        return sendMessageAndTagsCallback(broker, msg_key, message, planTime, receiver_url, receiver_cehck, null);
    }

    public boolean sendMessageAndTagsCallback(String broker, String message, String receiver_url, String receiver_cehck, String tags) throws Exception {
        return sendMessageAndTagsCallback(broker, null, message, null, receiver_url, receiver_cehck, tags);
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
    public boolean sendMessageAndTagsCallback(String broker, String msg_key, String message, Date planTime, String receiver_url, String receiver_cehck, String tags) throws Exception {
        if (TextUtils.isEmpty(msg_key)) {
            msg_key = IDUtils.guid();
        }

        if (tags == null) {
            tags = "";
        }

        Map<String, String> params = new HashMap<>();

        if (TextUtils.isNotEmpty(broker)) {
            params.put("broker", broker);
        }

        params.put("key", msg_key);
        params.put("tags", tags);
        params.put("message", message);
        params.put("receiver_url", receiver_url);
        params.put("receiver_cehck", receiver_cehck);


        if (planTime != null) {
            params.put("plan_time", String.valueOf(planTime.getTime()));
        }

        String txt = apiCaller.post("/msg/send/", params);

        ONode data = ONode.loadStr(txt);
        int code = data.get("code").getInt();

        if (code == 1 || code == 200) {
            return true;
        } else {
            throw new RuntimeException("消息发送失败:" + data.toJson());
        }
    }

    /**
     * 取消消息
     */
    public boolean cancelMessage(String broker, String msg_key) throws Exception {
        return cancelMessage(broker, msg_key, null);
    }

    /**
     * 取消XXX订阅者的消息
     */
    public boolean cancelMessage(String broker, String msg_key, String subscriber_key) throws Exception {
        Map<String, String> params = new HashMap<>();

        if (TextUtils.isNotEmpty(broker)) {
            params.put("broker", broker);
        }

        params.put("key", msg_key);

        if (TextUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }

        String txt = apiCaller.post("/msg/cancel/", params);

        int code = ONode.loadStr(txt).get("code").getInt();
        return code == 1 || code == 200;
    }

    /**
     * 完成消息（设为成功）
     */
    public boolean succeedMessage(String broker, String msg_key) throws Exception {
        return succeedMessage(broker, msg_key, null);
    }

    /**
     * 完成XXX订阅者的消息
     */
    public boolean succeedMessage(String broker, String msg_key, String subscriber_key) throws Exception {
        Map<String, String> params = new HashMap<>();

        if (TextUtils.isNotEmpty(broker)) {
            params.put("broker", broker);
        }

        params.put("key", msg_key);

        if (TextUtils.isEmpty(subscriber_key) == false) {
            params.put("subscriber_key", subscriber_key);
        }


        String txt = apiCaller.post("/msg/succeed/", params);

        int code = ONode.loadStr(txt).get("code").getInt();
        return code == 1 || code == 200;
    }

    /**
     * 检测消息签名
     */
    public boolean checkMessage(MessageM msg, String receive_key) {

        if (TextUtils.isEmpty(msg.topic)) {
            return false;
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.key).append("#");
        sb.append(msg.topic).append("#");
        sb.append(msg.message).append("#");
        sb.append(receive_key);

        String sgin_slf = EncryptUtils.md5(sb.toString());

        return sgin_slf.equals(msg.sgin);
    }

    public String receiveMessage(Fun1<String, String> paramS, String service_secretKey, MessageHandler consumer) throws Throwable {
        MessageM msg = new MessageM(paramS);

        if (checkMessage(msg, service_secretKey) == false) {
            return "CHECK ERROR";
        }

        boolean isOk = consumer.handle(msg);

        return isOk ? "OK" : "ERROR";
    }
}