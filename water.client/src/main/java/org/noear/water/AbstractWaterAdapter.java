package org.noear.water;

import org.noear.water.model.MessageM;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.LocalUtils;
;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;

/**
 * Water 适配器
 *
 * @author noear
 * @since 2.0
 * */
public abstract class AbstractWaterAdapter {
    /** 报警手机号 :: 需要重写 */
    public abstract String alarm_mobile();

    /** 服务名称 :: 需要重写 */
    public abstract String service_name();

    /** 服务空间 :: 可以重写 */
    public String service_tag(){ return null; }

    /** 服务密钥 :: 可以重写 */
    public String service_secretKey() {
        return null;
    }

    /** 是否为稳定服务 :: 可以重写 */
    public boolean is_unstable(){ return false;}

    public String localHost(){
        return null;
    }

    //由子类附值
    protected String service_status_path = null;
    protected String service_check_path = null;
    protected String service_stop_path = null;
    protected String msg_receiver_path = null;
    protected int service_port = 0;

    private String msg_receiver_url_local() {
        String temp = "http://" + getLocalAddress(service_port) + msg_receiver_path;
        temp = temp.replace("//", "/").replace(":/", "://");

        return temp;
    }


    protected void registerService() {
        if (service_port > 0 && TextUtils.isEmpty(service_check_path) == false) {
            //::开始进行初始化
            String local_host = getLocalAddress(service_port);

            //1.注册到服务
            WaterClient.Registry.register(service_name(), local_host, service_check_path, alarm_mobile(), is_unstable());
        }
    }

    protected void messageSubscribe() {
        if(TextUtils.isEmpty(msg_receiver_path) == false) {
            //2.订阅内部消息
            try {
                messageSubscribeTopicLocal(0, WW.msg_ucache_topic, WW.msg_uconfig_topic);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        messageSubscribeHandler();
    }

    //1.订阅消息的处理 //可以重写
    public void messageSubscribeHandler() {
    }

    //1.1.订阅消息封装，供使用
    private void messageSubscribeTopicLocal(int receive_way, String... topics) throws Exception {
        if(TextUtils.isEmpty(service_secretKey())){ //有没有密钥?
            return;
        }

        String msg_receiver_url_local = msg_receiver_url_local();
        String msg_subscriber_id_local = EncryptUtils.md5(service_name()+ msg_receiver_url_local);

        WaterClient.Message.subscribeTopic(msg_subscriber_id_local,
                service_name(), //本地URL订阅，添加服务名
                msg_receiver_url_local,
                service_secretKey(),
                alarm_mobile(),
                receive_way,
                is_unstable(), //内网订阅，可能是不稳定地址
                topics);
    }

    public void messageSubscribeTopic(String msg_receiver_url,int receive_way, String... topics) throws Exception {
        if(TextUtils.isEmpty(msg_receiver_url)){
            return;
        }

        if(TextUtils.isEmpty(service_name())){
            return;
        }

        WaterClient.Message.subscribeTopic(service_name(),
                msg_receiver_url,
                service_secretKey(),
                alarm_mobile(),
                receive_way,
                false, //服务订阅，为稳定地址
                topics);
    }

    //X.更新配置的处理 //可以重写
    public void configUpdateHandler(String tag, String name) {
    }

    public void cacheUpdateHandler(String tag){

    }


    protected String doMessageReceive(Fun1<String, String> paramS) throws Throwable {
        return WaterClient.Message.receiveMessage(paramS, service_secretKey(), (msg) -> {
            boolean isOk = messageReceiveForInner(msg);

            return messageReceiveHandler(msg) || isOk;
        });
    }


    //2.2.2.处理消息 //可以重写
    public boolean messageReceiveHandler(MessageM msg) throws Throwable {
        return true;
    }


    //2.2.1.内部消息处理
    protected boolean messageReceiveForInner(MessageM msg) {
        if (WW.msg_ucache_topic.equals(msg.topic) == false &&
                WW.msg_uconfig_topic.equals(msg.topic) == false) {
            return false;
        }

        try {
            String[] tag_keys = msg.message.split(";");

            //更新缓存
            if (WW.msg_ucache_topic.equals(msg.topic)) {
                //调用缓存处理
                for (String tag : tag_keys) { //xxx.xxx_xxx
                    if (TextUtils.isEmpty(tag) == false) {
                        cacheUpdateHandler(tag);
                    }
                }
            }

            //更新配置
            if (WW.msg_uconfig_topic.equals(msg.topic)) {
                for (String tagKey : tag_keys) {//xxx::bbb
                    String[] ss = tagKey.split("::");

                    if (ss.length > 1) {
                        WaterClient.Config.reload(ss[0]);
                        configUpdateHandler(ss[0], ss[1]);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    protected static String getLocalAddress(int port) {
        String host = null;
        try {
            host = LocalUtils.getLocalIp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (port > 0) {
            return host + ":" + port;
        } else {
            return host;
        }
    }
}
