package waterapi.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import waterapi.controller.cmds.CMD_log_add;
import waterapi.controller.cmds.CMD_log_add2;

/**
 * Created by noear on 2017/7/18.
 */
@XMapping("/log/**")
@XController
public class LogController implements XHandler {
    @Override
    public void handle(XContext c) throws Exception {
        String path = c.path();

        if ("/log/add/".equals(path)) {
            new CMD_log_add().exec(c);
        } else if ("/log/add2/".equals(path)) {
            new CMD_log_add2().exec(c);
        }
    }
}
