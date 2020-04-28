package waterapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import waterapp.controller.cmds.*;

/**
 * Created by noear on 2017/7/17.
 */
@XMapping("/msg/**")
@XController
public class MsgController implements XHandler {
    @Override
    public void handle(XContext c) throws Exception {
        String path = c.path();
        switch (path){
            case "/msg/subscribe/":
                new CMD_msg_subscribe().exec(c);
                break;
            case "/msg/unsubscribe/":
                new CMD_msg_unsubscribe().exec(c);
                break;
            case "/msg/send/":
                new CMD_msg_send().exec(c);
                break;
            case "/msg/cancel/":
                new CMD_msg_cancel().exec(c);
                break;
            case "/msg/succeed/":
                new CMD_msg_succeed().exec(c);
                break;
            case "/msg/set/topic/":
                new CMD_msg_set_topic().exec(c);
                break;
            default:
                break;
        }
    }
}
