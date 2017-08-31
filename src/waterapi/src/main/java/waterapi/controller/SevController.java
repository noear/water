package waterapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import waterapi.Config;
import waterapi.controller.cmds.CMD_reg_sev;
import waterapi.controller.cmds.CMD_reg_sev_view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuety on 2017/7/18.
 */
@RestController
public class SevController {
    @RequestMapping("/sev/reg/")
    public void sev_reg(HttpServletRequest request, HttpServletResponse response) {
        Config.tryInit(request);

        new CMD_reg_sev().exec(request,response);
    }

    @RequestMapping("/sev/view/")
    public void msg_view(HttpServletRequest request, HttpServletResponse response) {
        Config.tryInit(request);


        new CMD_reg_sev_view().exec(request, response);
    }
}
