package waterfaas.controller._msg;

import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.annotation.EventLevel;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.core.handle.Context;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.water.config.ServerConfig;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;
import waterfaas.dso.AFileUtil;
import waterfaas.dso.db.DbLuffyApi;
import waterfaas.dso.RouteHelper;
import waterfaas.dso.db.DbWaterCfgSafeApi;

/**
 * 实时更新FaaS代码
 * */
@CloudEvent(topic = "water.cache.update", level = EventLevel.instance)
public class msg_updatecache implements CloudEventHandler {
    static final String label_hook_start = "hook.start";

    @Override
    public boolean handle(Event event) throws Throwable {
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
                    AFileModel file = DbLuffyApi.fileGet(Integer.parseInt(file_id));

                    if (Utils.isNotEmpty(file.path)) {
                        //更新代码缓存
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);

                        RouteHelper.reset();

                        //处理hook.start
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

            if ("whitelist".equals(ss[0])) {
                ServerConfig.taskToken = DbWaterCfgSafeApi.getServerTokenOne();
            }
        }
    }
}
