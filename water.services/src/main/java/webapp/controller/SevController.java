package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import webapp.controller.cmds.*;

@XMapping("/sev/**")
@XController
public class SevController implements XHandler {
    @Override
    public void handle(XContext c) throws Exception {
        String path = c.path();
        switch (path) {
            case "/sev/reg/":
                new CMD_sev_reg().exec(c);
                break;
            case "/sev/set/":
                new CMD_sev_set().exec(c);
                break;
            case "/sev/discover/":
                new CMD_sev_discover().exec(c);
                break;
            case "/sev/track/":
                new CMD_sev_track_api().exec(c);
                break;

            case "/sev/track/api/":
                new CMD_sev_track_api().exec(c);
                break;
            case "/sev/track/sql/":
                new CMD_sev_track_sql().exec(c);
                break;
            default:
                break;
        }
    }
}
