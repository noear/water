package watersev.controller._msg;

import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.annotation.EventLevel;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.core.handle.Context;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import watersev.dso.AFileUtil;
import watersev.dso.DbPaaSApi;

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
            if ("paas".equals(ss[0])) {
                String file_id = ss[1];

                if (TextUtils.isNumeric(file_id)) {
                    AFileModel file = DbPaaSApi.fileGet(Integer.parseInt(file_id));

                    if (TextUtils.isEmpty(file.path) == false) {
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);

                        //处理hook.start
                        //
                        if (label_hook_start.equals(file.label)) {
                            ExecutorFactory.execOnly(file, Context.current());
                        }
                    }
                }
                return;
            }

            if ("logger".equals(ss[0])) {
                if (ProtocolHub.logSourceFactory != null) {
                    ProtocolHub.logSourceFactory.updateSource(ss[1]);
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
}
