package wateradmin.controller._msg;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.annotation.EventLevel;
import org.noear.solon.cloud.impl.CloudLoadBalance;
import org.noear.solon.cloud.impl.CloudLoadBalanceFactory;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.cloud.utils.http.HttpUtils;
import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;

/**
 * 实时更新FaaS代码
 * */
@Slf4j
@CloudEvent(topic = "water.cache.update", level = EventLevel.instance)
public class msg_updatecache implements CloudEventHandler {
    static final String label_hook_start = "hook.start";

    @Override
    public boolean handler(Event event) throws Throwable {
        for (String tag : event.content().split(";")) {
            if (TextUtils.isEmpty(tag) == false) {
                handlerDo(tag);
            }
        }

        return true;
    }

    private void handlerDo(String tag) throws Exception {
        if (tag.indexOf(":") > 0) {
            String[] ss = tag.split(":");

            if ("logger".equals(ss[0])) {
                if (ProtocolHub.logSourceFactory != null) {
                    ProtocolHub.logSourceFactory.updateSource(ss[1]); //尝试更新源
                }

                loggerUpdateOfApi(ss[1]);
                return;
            }
        }
    }


    private void loggerUpdateOfApi(String logger) {
        CloudLoadBalance loadBalance = CloudLoadBalanceFactory.instance.get("water", WW.waterapi);
        for (Instance instance : loadBalance.getDiscovery().cluster()) {
            try {
                HttpUtils.http("http://" + instance.address() + "/log/logger/")
                        .data("logger", logger)
                        .post();

                log.error("Logger source update succeed: {}", instance.address());
            } catch (Exception e) {
                log.error("Logger source update error: {}:\r\n{}", instance.address(), e);
            }
        }
    }
}
