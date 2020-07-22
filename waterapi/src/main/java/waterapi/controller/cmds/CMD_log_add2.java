package waterapi.controller.cmds;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.water.WW;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.GzipUtils;
import waterapi.dso.LogUtils;

import java.util.List;

/**
 * Created by noear on 2017/7/19.
 *
 * 使用 post body 提交(content type : application/x-gzip)
 */
public class CMD_log_add2 extends CMDBase {

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        if(WW.mime_gzip.equals(context.contentType()) == false){
           return;
        }

        String list_json = GzipUtils.uncompressToString(context.bodyAsBytes());

        if (checkParamsIsOk(list_json) == false) {
            LogUtils.warn(context, "ctx.bodyAsBytes() is null");
            return;
        }

        List<LogEvent> list = ONode.deserialize(list_json);

        ProtocolHub.logStorer.writeAll(list);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
