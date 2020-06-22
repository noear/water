package waterapp.controller._msg;

import org.noear.solon.core.XContext;
import org.noear.solonjt.executor.ExecutorFactory;
import org.noear.solonjt.model.AFileModel;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.model.MessageM;
import org.noear.water.solon_plugin.XMessageHandler;
import org.noear.water.utils.StringUtils;
import org.noear.water.utils.TextUtils;
import waterapp.dso.AFileUtil;
import waterapp.dso.DbPaaSApi;

@WaterMessage("water.cache.update")
public class msg_updatecache implements XMessageHandler {
    static final String label_hook_start = "hook.start";

    @Override
    public boolean handler(MessageM msg) throws Exception {
        for (String tag : msg.message.split(";")) {
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
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);

                        //处理hook.start
                        //
                        if (label_hook_start.equals(file.label)) {
                            ExecutorFactory.execOnly(file, XContext.current());
                        }
                    }
                }
            }
        }
    }
}
