package waterraas.controller.release;

import org.noear.rubber.Rubber;
import org.noear.rubber.models.LogRequestModel;
import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.Timecount;
import waterraas.Config;
import waterraas.controller.SystemCode;
import waterraas.dao.LogUtil;
import waterraas.dao.SchemeUtil;

//::/([^\/]+)/([^\/]+)
public class SchemeController implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        String scheme = context.param("scheme"); //通过参数传入
        if (scheme == null) {
            scheme = context.path().substring(3); //通过路径传入
        }

        String callback = context.param("callback");
        String request_id = context.param("request_id");
        String args_str = context.param("args");
        int policy = context.paramAsInt("policy", 1001);

        if (TextUtils.isEmpty(scheme)) {
            ONode data = new ONode();
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        if (TextUtils.isEmpty(args_str)) {
            //如果没有参数，尝试检测有没有已处理的记录
            if (TextUtils.isEmpty(request_id) == false) {
                if (tryOut(context, request_id)) {
                    return;
                }
            }


            ONode data = new ONode();
            data.set("code", 10).set("msg", SystemCode.code_10);
            context.output(data.toJson());
            return;
        }

        try {
            Timecount timecount = new Timecount().start();

            if (TextUtils.isEmpty(callback) == false) {
                SchemeUtil.asynRun(context, request_id, scheme, args_str, policy, callback);
            } else {
                SchemeUtil.run(context, request_id, scheme, policy, args_str, 0, null, false);
            }

            long timespan = timecount.stop().milliseconds();
            WaterClient.Track.track(Config.water_service_name, "scheme", scheme, timespan, WaterClient.localServiceHost());

        } catch (Exception ex) {
            ONode data = new ONode();
            data.set("code", 0).set("msg", ex.getMessage());
            LogUtil.logSchemeError(scheme, args_str, ex);
        }
    }

    private static boolean tryOut(Context context, String request_id) throws Exception {
        LogRequestModel req = Rubber.get(request_id);
        if (req.log_id > 0) {
            SchemeUtil.out(context, req);
            return true;
        } else {
            return false;
        }
    }
}
