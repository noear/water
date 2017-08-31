package waterapi.controller;

import org.springframework.web.bind.annotation.*;
import waterapi.Config;
import waterapi.controller.cmds.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuety on 2017/7/17.
 */
@RestController
public class MsgController {
    @RequestMapping("/msg/subscribe/")
    public void msg_subscribe(HttpServletRequest request, HttpServletResponse response) {
        new CMD_msg_subscribe().exec(request, response);
    }

    @RequestMapping("/msg/unsubscribe/")
    public void msg_unsubscribe(HttpServletRequest request, HttpServletResponse response) {
        new CMD_msg_unsubscribe().exec(request, response);
    }

    @RequestMapping("/msg/send/")
    public void msg_send(HttpServletRequest request, HttpServletResponse response) {
        new CMD_msg_send().exec(request, response);
    }

    @RequestMapping("/msg/cancel/")
    public void msg_cancel(HttpServletRequest request, HttpServletResponse response) {
        new CMD_msg_cancel().exec(request, response);
    }

    @RequestMapping("/msg/view/")
    public void msg_view(HttpServletRequest request, HttpServletResponse response) {
        new CMD_msg_view().exec(request, response);
    }
}
