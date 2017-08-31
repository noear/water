package waterapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import waterapi.Config;
import waterapi.controller.cmds.CMD_reg_sev;
import waterapi.controller.cmds.CMD_reg_sev_view;
import waterapi.controller.cmds.CMD_reg_sync;
import waterapi.controller.cmds.CMD_reg_sync_view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yuety on 2017/7/18.
 */
@RestController
public class RegController {
    @RequestMapping("/reg/sev/")
    public void reg_sev(HttpServletRequest request, HttpServletResponse response) {
        Config.tryInit(request);

        new CMD_reg_sev().exec(request,response);
    }

    @RequestMapping("/reg/sev/view/")
    public void reg_sev_view(HttpServletRequest request, HttpServletResponse response) {
        Config.tryInit(request);


        new CMD_reg_sev_view().exec(request, response);
    }

    //==============================================

    @RequestMapping("/reg/sync/")
    public void reg_sync(HttpServletRequest request, HttpServletResponse response){
        Config.tryInit(request);

        new CMD_reg_sync().exec(request,response);
    }

    @RequestMapping("/reg/sync/view/")
    public void reg_sync_view(HttpServletRequest request, HttpServletResponse response){
        Config.tryInit(request);

        new CMD_reg_sync_view().exec(request,response);
    }
}
