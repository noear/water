package org.noear.rubber;

import org.noear.snack.ONode;

public final class RubberRequest {
    public String request_id;//请求ID（要求全局唯一，与业务系弘对接使用）
    public String scheme_tagName;
    public ONode args; //用于log
    public int policy;

    protected RubberRequest(String request_id, String scheme_tagName, int policy, ONode args){
        this.request_id = request_id;
        this.scheme_tagName = scheme_tagName;
        this.policy = policy;
        this.args  = args;
    }

    protected RubberRequest(String request_id, int policy, ONode args){
        this.request_id = request_id;
        this.scheme_tagName = "";
        this.policy = policy;
        this.args  = args;
    }
}
