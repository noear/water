package waterapi.controller.cmds;

import org.noear.snack.ONode;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.ProtocolHub;

import java.util.List;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_log_add2 extends CMDBase {
    static final String log_fulltime_formt = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String list_json = get("list");

        if (checkParamsIsOk(list_json) == false) {
            return;
        }

        List<LogEvent> list = ONode.deserialize(list_json);

        ProtocolHub.logStorer.writeAll(list);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
