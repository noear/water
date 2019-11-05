package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import webapp.controller.cmds.CMD_sev_reg;

@XMapping("/reg/**")
@XController
public class RegController implements XHandler {
    @Override
    public void handle(XContext c) throws Exception {
        String path = c.path();

        if("/reg/sev/".equals(path)){
            new CMD_sev_reg().exec(c);
        }
    }
}
