package waterraas.dao;

import org.noear.rubber.Rubber;
import org.noear.rubber.RubberException;
import org.noear.rubber.RubberResponse;
import org.noear.rubber.models.LogRequestModel;
import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;
import waterraas.Config;
import waterraas.controller.SystemCode;

public class SchemeUtil {
    public static void run(Context context, String request_id, String scheme, int policy, String args_str, int type, String rule, boolean is_debug) throws Exception {
        if (TextUtils.isEmpty(request_id)) {
            request_id = Rubber.guid();
        }

        ONode data = new ONode();
        ONode args = ONode.load(args_str);

        data.set("code", 1).set("msg", SystemCode.code_1);

        ONode jReq = data.get("request");
        jReq.set("request_id", request_id);
        jReq.set("scheme", scheme);
        jReq.set("type", type);
        if (TextUtils.isEmpty(rule) == false) {
            jReq.set("rule", rule);
        }
        jReq.set("args", args);
        jReq.set("policy", policy);

        ONode jResp = data.get("response");

        try {
            RubberResponse resp2 = run(0, request_id, scheme, policy, args, type, rule, is_debug);

            if (is_debug) {
                jResp.set("matcher", resp2.matcher_json());
                jResp.set("evaluation", resp2.evaluation_json());
                jResp.set("session", resp2.session_json);
                jResp.set("model", resp2.model_json);
            } else {
                if (policy / 1000 == 1) {
                    jResp.set("matcher", resp2.matcher_json());
                } else {
                    jResp.set("evaluation", resp2.evaluation_json());
                }
                jResp.set("session", resp2.session_json);
            }

        } catch (RubberException ex) {
            data.set("code", 11).set("msg", SystemCode.code_11(ex.getMessage()) );
        } catch (Exception ex) {

            if (is_debug) {
                data.set("code", 0).set("msg", SystemCode.code_0);
                jResp.set("hint", ThrowableUtils.getString(ex));
            } else {
                data.set("code", 0).set("msg", ex.getMessage());
                LogUtil.logSchemeError(scheme, args_str, ex);
            }
        }

        context.output(data.toJson());
    }

    public static RubberResponse run(long log_id, String request_id, String scheme, int policy, ONode args, int type, String rule, boolean is_debug) throws Exception {
        RubberResponse resp = Rubber.scheme(request_id, scheme, policy, args, type, rule, is_debug);

        if (TextUtils.isEmpty(rule)) { //只有单条规则调试时，才不记日志
            Rubber.log(log_id, resp);
        }

        return resp;
    }

    public static void asynRun(Context context, String request_id, String scheme, String args_str, int policy, String callback) throws Exception {
        if (TextUtils.isEmpty(request_id)) {
            request_id = Rubber.guid();
        }

        //创建任务记录
        long log_id = Rubber.add(request_id, scheme, args_str, policy, callback);

        //发送消息
        ONode msg = new ONode();
        msg.set("request_id", request_id);
        msg.set("scheme", scheme);
        msg.set("log_id", log_id);

        WaterClient.Message.sendMessage(Config.msg_rubber_task, msg.toJson());

        //返回结果
        ONode data = new ONode();
        data.set("code", 1).set("msg", SystemCode.code_1);
        data.set("request",msg);

        context.output(data.toJson());
    }

    public static void out(Context context, LogRequestModel log) throws Exception {
        ONode data = new ONode();

        data.set("code", 1).set("msg", SystemCode.code_1);

        ONode args = ONode.load(log.args_json);

        ONode jReq = data.get("request");
        jReq.set("request_id", log.request_id);
        jReq.set("scheme", log.scheme_tagname);
        jReq.set("args", args);
        jReq.set("policy", log.policy);

        ONode jResp = data.get("response");

        if (log.state >= 2) { //说明已完成
            if (log.policy / 1000 == 1) {
                jResp.set("matcher", ONode.load(log.matcher_json));
            } else {
                jResp.set("evaluation", ONode.load(log.evaluation_json));
            }
            jResp.set("session", ONode.load(log.session_json));
        } else {
            data.set("code", 2).set("msg", SystemCode.code_2);

            jResp.set("state", log.state);
        }

        context.output(data.toJson());
    }
}
