package org.noear.water.model;

import org.noear.snack.ONode;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun1;

public class MessageM {
    public final int times;
    public final String trace_id;
    public final String key;
    public final String topic;
    public final String message;
    public final String sgin;
    public final String tags;

    public MessageM(Fun1<String, String> args) {
        this.key = args.run("key");

        if (TextUtils.isEmpty(this.key)) {
            this.topic = null;
            this.times = 0;
            this.trace_id = null;
            this.tags = null;
            this.sgin = null;
            this.message = null;
        } else {
            this.topic = args.run("topic");
            String _times = args.run("times");
            if (TextUtils.isEmpty(_times) == false) {
                this.times = Integer.parseInt(_times);
            } else {
                this.times = 0;
            }
            this.trace_id = args.run("trace_id");
            this.tags = args.run("tags");
            this.sgin = args.run("sgin");

            this.message = Base64Utils.decode(args.run("message"));
        }
    }

    public String toJson() {
        ONode d = new ONode();

        d.set("key", key);
        d.set("topic", topic);
        d.set("trace_id", trace_id);
        d.set("message", message);
        d.set("tags", tags);
        d.set("sgin", sgin);

        return d.toJson();
    }
}
