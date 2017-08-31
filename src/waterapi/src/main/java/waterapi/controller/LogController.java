package waterapi.controller;

import org.springframework.web.bind.annotation.*;
import waterapi.Config;
import waterapi.controller.cmds.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by yuety on 2017/7/18.
 */

@RestController
public class LogController {
    @RequestMapping("/log/add/")
    public void log_add(HttpServletRequest request, HttpServletResponse response) {
        Config.tryInit(request);
        new CMD_log_add().exec(request,response);
    }

    @GetMapping("/log/new/")
    public void log_new_logger(HttpServletRequest request, HttpServletResponse response) {
        new CMD_log_new().exec(request,response);
    }

    @GetMapping("/log/view/{logger}/")
    public void log_view(HttpServletRequest request, HttpServletResponse response, @PathVariable("logger") String logger) {
        Config.tryInit(request);

        CMD_log_view cmd = new CMD_log_view();
        cmd.logger = logger;
        cmd.exec(request, response);
    }
}
