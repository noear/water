package waterraas.controller._msg;

import org.noear.rubber.Rubber;
import org.noear.rubber.models.LogRequestModel;
import org.noear.snack.ONode;
import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.model.Event;
import org.noear.water.utils.HttpUtils;
import waterraas.dso.LogUtil;

import java.util.HashMap;
import java.util.Map;

@CloudEvent("rubber.notice")
public class msg_rubber_notice implements CloudEventHandler {
    @Override
    public boolean handler(Event event) throws Throwable {
        ONode jReq = ONode.load(event.content());
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
