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
import waterapp.dso.DbApi;
import waterapp.dso.RouteHelper;

@WaterMessage("water.cache.update")
public class msg_updatecache implements XMessageHandler {
    static final String label_hook_start = "hook.start";

    @Override
    public boolean handler(MessageM msg) throws Exception {
        for(String pk : msg.message.split(";")){
            if(TextUtils.isEmpty(pk) == false){
                handlerDo(pk);
            }
        }

        return true;
    }

    private void handlerDo(String pk) throws Exception{
        if (pk.indexOf(":") > 0) {
            String[] ss = pk.split(":");
            if ("paas".equals(ss[0])) {
                String file_id = ss[1];

                if (StringUtils.isNumeric(file_id)) {
                    AFileModel file = DbApi.fileGet(Integer.parseInt(file_id));

                    if (TextUtils.isEmpty(file.path) == false) {
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);

                        RouteHelper.reset();

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
