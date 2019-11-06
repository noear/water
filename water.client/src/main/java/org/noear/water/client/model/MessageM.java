package org.noear.water.client.model;

import org.noear.water.tools.Base64Utils;
import org.noear.water.tools.EncryptUtils;
import org.noear.water.tools.StringUtils;
import org.noear.water.tools.ext.Fun1;

/**
 * 消息模型
 * */
public class MessageM {
    /** 消息id */
    public long id;
    /** 超时 */
    public int times;
    /** 消息key */
    public String key;
    /** 消息主题 */
    public String topic;
    /** 消息内容 */
    public String message;
    /** 签名 */
    public String sgin;

    /**
     * 初始化
     * */
    public MessageM(Fun1<String,String> args) {
        this.key = args.run("key");

        if (StringUtils.isEmpty(this.key)) {
            return;
        }

        this.id = Long.parseLong(args.run("id"));
        this.topic = args.run("topic");
        String _times = args.run("times");
        if(StringUtils.isEmpty(_times)==false){
            this.times = Integer.parseInt(_times);
        }
        this.message = args.run("message");
        this.sgin = args.run("sgin");

        this.message = Base64Utils.decode(this.message);
    }

    /**
     * 检查是否ok
     * */
    public boolean checkIsOk(String receive_secret){
        if (id < 1) {
            return false;
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append(id).append("#");
        sb.append(key).append("#");
        sb.append(topic).append("#");
        sb.append(message).append("#");
        sb.append(receive_secret);

        String sgin_slf = EncryptUtils.md5(sb.toString());

        return sgin_slf.equals(sgin);
    }
}
