package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import webapp.controller.cmds.CMD_log_add;

@XMapping("/log/**")
@XController
public class LogController implements XHandler {
    @Override
    public void handle(XContext c) throws Exception {
        String path = c.path();

        if ("/log/add/".equals(path)) {
            new CMD_log_add().exec(c);
        }
    }
}
