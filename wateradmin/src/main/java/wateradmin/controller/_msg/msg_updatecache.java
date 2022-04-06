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

@Slf4j
@CloudEvent(topic = "water.cache.update", level = EventLevel.instance)
public class msg_updatecache implements CloudEventHandler {

    @Override
    public boolean handle(Event event) throws Throwable {
        for (String tag : event.content().split(";")) {
            if (TextUtils.isEmpty(tag) == false) {
                handlerDo(tag);
            }
        }

        cachedUpdateOfApi(event.content());

        return true;
    }

    private void handlerDo(String tag) throws Exception {
        if (tag.indexOf(":") > 0) {
            String[] ss = tag.split(":");

            if ("logger".equals(ss[0])) {
                if (ProtocolHub.logSourceFactory != null) {
                    ProtocolHub.logSourceFactory.updateSource(ss[1]); //尝试更新源
                }
                return;
            }

            if ("broker".equals(ss[0])) {
                if (ProtocolHub.msgBrokerFactory != null) {
                    ProtocolHub.msgBrokerFactory.updateBroker(ss[1]); //尝试更新源
                }
                return;
            }
        }
    }


    private void cachedUpdateOfApi(String tags) {
        if (TextUtils.isEmpty(tags)) {
            return;
        }

        if (tags.contains("logger:") == false
                && tags.contains("broker:") == false) {
            return;
        }

        CloudLoadBalance loadBalance = (CloudLoadBalance) CloudLoadBalanceFactory.instance.create("water", WW.waterapi);

        for (Instance instance : loadBalance.getDiscovery().cluster()) {
            try {
                HttpUtils.http("http://" + instance.address() + "/run/cache/update/")
                        .data("tags", tags)
                        .post();

                log.info("Cached update succeed: {} - @{}", tags, instance.address());
            } catch (Exception e) {
                log.error("Cached update error: {} - @{}:\r\n{}", tags, instance.address(), e);
            }
        }
    }
}
