package waterraas.controller._msg;

import org.noear.rubber.Rubber;
import org.noear.rubber.models.LogRequestModel;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.MessageHandler;
import org.noear.water.model.MessageM;
import waterraas.Config;
import waterraas.dao.LogUtil;
import waterraas.dao.SchemeUtil;

@WaterMessage("rubber.task")
public class msg_rubber_task implements MessageHandler {

    @Override
    public boolean handler(MessageM msg) throws Exception {
        ONode jReq = ONode.load(msg.message);
        String request_id = jReq.get("request_id").getString();
        LogRequestModel log = Rubber.get(request_id);

        if (log.log_id > 0) {
            if (log.state == 0) { //等于0才计算
                Rubber.setBegin(log.log_id);
                ONode args = ONode.load(log.args_json);

                try {
                    SchemeUtil.run(log.log_id, request_id, log.scheme_tagname, log.policy, args, 0, null, false);

                    if (log.callback != null && log.callback.indexOf("://") > 0) {
                        WaterClient.Message.sendMessage(Config.msg_rubber_notice, msg.message);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Rubber.setRestart(log.log_id);
                    LogUtil.logSchemeError(log.scheme_tagname, log.args_json, ex);
                    return false;
                }
            } else {
                return log.state >= 2;//2以上算ok，以下算no
            }
        }

        return false;
    }
}
