package org.noear.water.client.model;

import org.noear.snack.ONode;
import org.noear.water.client.utils.Base64Utils;
import org.noear.water.client.utils.StringUtils;
import org.noear.water.client.utils.ext.Fun1;

/**
 * 消息模型
 * */
public class MessageModel {
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

    public MessageModel(Fun1<String,String> args) {
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

    public String toJson(){
        ONode d = new ONode();

        d.set("id",id);
        d.set("key",key);
        d.set("topic",topic);
        d.set("message",message);
        d.set("sgin",sgin);

        return d.toJson();
    }
}
