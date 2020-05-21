package waterapp.controller._msg;

import org.noear.solonjt.executor.ExecutorFactory;
import org.noear.solonjt.model.AFileModel;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.model.MessageM;
import org.noear.water.solon_plugin.XMessageHandler;
import org.noear.water.utils.StringUtils;
import org.noear.water.utils.TextUtils;
import waterapp.dso.AFileUtil;
import waterapp.dso.DbApi;

@WaterMessage("water.cache.update")
public class msg_updatecache implements XMessageHandler {
    @Override
    public boolean handler(MessageM msg) throws Exception {
        if (msg.message.indexOf(":") > 0) {
            String[] ss = msg.message.split(":");
            if ("paas".equals(ss[0])) {
                String file_id = ss[1];

                if (StringUtils.isNumeric(file_id)) {
                    AFileModel file = DbApi.fileGet(Integer.parseInt(file_id));

                    if (TextUtils.isEmpty(file.path) == false) {
                        String name = file.path.replace("/", "__");
                        AFileUtil.remove(file.path);
                        ExecutorFactory.del(name);
                    }
                }
            }
        }

        return true;
    }
}
