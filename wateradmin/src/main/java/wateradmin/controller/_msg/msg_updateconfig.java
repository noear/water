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
import org.noear.water.utils.TextUtils;

/**
 * @author noear 2022/5/28 created
 */
@Slf4j
@CloudEvent(topic = "water.config.update", level = EventLevel.instance)
public class msg_updateconfig implements CloudEventHandler {
    @Override
    public boolean handle(Event event) throws Throwable {
        configUpdateOfApi(event.content());

        return true;
    }

    private void configUpdateOfApi(String tags) {
        if (TextUtils.isEmpty(tags)) {
            return;
        }

        if (tags.contains("water::") == false) {
            return;
        }

        CloudLoadBalance loadBalance = (CloudLoadBalance) CloudLoadBalanceFactory.instance.create("water", WW.waterapi);

        for (Instance instance : loadBalance.getDiscovery().cluster()) {
            try {
                HttpUtils.http("http://" + instance.address() + "/_run/update/config/")
                        .data("tags", tags)
                        .post();

                log.info("Config update succeed: {} - @{}", tags, instance.address());
            } catch (Exception e) {
                log.error("Config update error: {} - @{}:\r\n{}", tags, instance.address(), e);
            }
        }
    }
}
