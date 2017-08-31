package waterapi.controller;

import noear.snacks.ONode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import waterapi.Config;
import waterapi.controller.cmds.CMD_run_msg_clean;
import waterapi.controller.cmds.CMD_run_msg_reset;
import waterapi.controller.cmds.CMD_run_push;
import waterapi.controller.cmds.CMD_run_sev_reset;
import waterapi.dao.db.DbApi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by yuety on 2017/7/18.
 */
@RestController
public class RunController {
    @GetMapping("/run/whitelist/reload/")
    public String run_whitelist_reload() {
        ONode data = new ONode();
        try {
            DbApi.loadWhitelist();
            data.set("code", 1);
            data.set("msg", "ok");
        } catch (Exception ex) {
            data.set("code", 0);
            data.set("msg", ex.getMessage());
        }

        return data.toJson();
    }

    @GetMapping("/run/msg/reset/")
    public void run_msg_reset(HttpServletRequest request,HttpServletResponse response) {
        Config.tryInit(request);
        new CMD_run_msg_reset().exec(request,response);
    }

    @GetMapping("/run/msg/clean/")
    public void run_msg_clean(HttpServletRequest request,HttpServletResponse response) {
        Config.tryInit(request);
        new CMD_run_msg_clean().exec(request,response);
    }

    @GetMapping("/run/sev/reset/")
    public void run_sev_reset(HttpServletRequest request,HttpServletResponse response) {
        Config.tryInit(request);
        new CMD_run_sev_reset().exec(request,response);
    }

    @GetMapping("/run/check/")
    public String run_check(HttpServletRequest request) throws SQLException {
        Config.tryInit(request);

        DbApi.loadWhitelist();

        ONode data = new ONode();

        data.set("code", 1);

        return data.toJson();
    }

    @GetMapping("/run/push/")
    public void run_push(HttpServletRequest request,HttpServletResponse response) {
        Config.tryInit(request);
        new CMD_run_push().exec(request,response);
    }
}
