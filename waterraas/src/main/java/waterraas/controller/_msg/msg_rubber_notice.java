package waterraas.controller._msg;

import org.noear.rubber.Rubber;
import org.noear.rubber.models.LogRequestModel;
import org.noear.snack.ONode;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.MessageHandler;
import org.noear.water.model.MessageM;
import org.noear.water.utils.HttpUtils;
import waterraas.dao.LogUtil;

import java.util.HashMap;
import java.util.Map;

@WaterMessage("rubber.notice")
public class msg_rubber_notice implements MessageHandler {
    @Override
    public boolean handler(MessageM msg) throws Exception {
        ONode jReq = ONode.load(msg.message);
        String request_id = jReq.get("request_id").getString();
        LogRequestModel log = Rubber.get(request_id);
        if (log.state == 2) {
            Map<String, String> data = new HashMap<>();
            data.put("request_id", request_id);

            String temp = HttpUtils.http(log.callback).data(data).post();

            if ("OK".equals(temp)) {
                Rubber.setEnd(log.log_id);
                return true;
            } else {
                LogUtil.logSchemeCallbackError(log.scheme_tagname, log.args_json, temp);
            }
        }
        return false;
    }
}
