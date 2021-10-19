package waterpaas.controller._msg;

import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.annotation.EventLevel;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.core.handle.Context;
import org.noear.luffy.executor.ExecutorFactory;
import org.noear.luffy.model.AFileModel;
import org.noear.water.utils.StringUtils;
import org.noear.water.utils.TextUtils;
import waterpaas.dso.AFileUtil;
import waterpaas.dso.DbPaaSApi;
import waterpaas.dso.RouteHelper;

/**
 * 实时更新FaaS代码
 * */
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

    private void handlerDo(String tag) throws Exception{
        if (tag.indexOf(":") > 0) {
            String[] ss = tag.split(":");
            if ("paas".equals(ss[0])) {
                String file_id = ss[1];

                if (StringUtils.isNumeric(file_id)) {
                    AFileModel file = DbPaaSApi.fileGet(Integer.parseInt(file_id));

                    if (TextUtils.isEmpty(file.path) == false) {
                        //
                        //更新代码缓存
                        //
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);

                        RouteHelper.reset();

                        //处理hook.start
                        //
                        if (label_hook_start.equals(file.label)) {
                            ExecutorFactory.execOnly(file, Context.current());
                        }
                    }
                }
            }
        }
    }
}
