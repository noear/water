package watersev.controller._msg;

import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.annotation.EventLevel;
import org.noear.solon.cloud.model.Event;
import org.noear.water.WW;
import org.noear.water.model.ConfigM;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import watersev.Config;

/**
 * @author noear 2022/5/28 created
 */
@CloudEvent(topic = "water.config.update", level = EventLevel.instance)
public class msg_updateconfig implements CloudEventHandler {
    @Override
    public boolean handle(Event event) throws Throwable {
        for (String tagKey : event.content().split(";")) {
            if (TextUtils.isEmpty(tagKey) == false) {
                handlerDo(tagKey);
            }
        }

        return true;
    }

    private void handlerDo(String tagKey) throws Exception {
        if (ProtocolHub.heihei == null) {
            return;
        }

        if (tagKey.indexOf("::") < 0) {
            return;
        }

        String[] tk = tagKey.split("::");

        if (WW.water.equals(tk[0])) {
            if (WW.water_heihei.equals(tk[1])) {
                ConfigM cfg = Config.cfg(WW.water_heihei);
                ProtocolHub.heihei.updateConfig(cfg);
            }
        }
    }
}
