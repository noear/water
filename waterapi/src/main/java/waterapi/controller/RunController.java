package waterapi.controller;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.water.WW;
import org.noear.water.utils.RuntimeUtils;
import waterapi.Config;
import waterapi.controller.cmds.CMD_run_push;
import waterapi.dso.IPUtils;
import waterapi.dso.db.DbWaterCfgApi;

import java.util.Map;


/**
 * Created by noear on 2017/7/18.
 */
@XMapping("/run/")
@XController
public class RunController {

    @XMapping("check/")
    public String run_check(XContext ctx) throws Exception {
        DbWaterCfgApi.loadWhitelist(); //检测服务时，就会进行白名单刷新

        ONode data = new ONode();

        data.set("code", 1);

        return data.toJson();
    }

    @XMapping("status/")
    public String run_status(XContext ctx) throws Exception{
        String ip = IPUtils.getIP(ctx);
        if (DbWaterCfgApi.isWhitelist(WW.whitelist_tag_master,"ip",ip)) {
            return ONode.stringify(RuntimeUtils.getStatus());
        } else {
            return  (ip + ",not is whitelist!");
        }
    }

    @XMapping("push/")
    public void run_push(XContext ctx) {
        new CMD_run_push().exec(ctx);
    }

    @XMapping("whitelist/reload/")
    public String run_whitelist_reload() {
        ONode data = new ONode();
        try {
            DbWaterCfgApi.loadWhitelist();
            data.set("code", 1);
            data.set("msg", "ok");
        } catch (Exception ex) {
            data.set("code", 0);
            data.set("msg", ex.getMessage());
        }

        return data.toJson();
    }

    @XMapping("whitelist/check/")
    public String run_whitelist_check(XContext ctx) throws Exception {
        String tags = ctx.param("tags", "");
        String type = ctx.param("type", "");
        String value = ctx.param("value", "");

        if(tags.contains("client")){
            if(DbWaterCfgApi.whitelistIgnoreClient()){
                return "OK";
            }
        }

        if (DbWaterCfgApi.isWhitelist(tags, type, value)) {
            return ("OK");
        } else {
            return (value + ",not is whitelist!");
        }
    }

    @XMapping("check/http/")
    public String run_check_http(XContext ctx) {
        ONode data = new ONode();

        Map<String, String> map = ctx.headerMap();
        if (map != null) {
            map.forEach((k, v) -> {
                data.set(k, v);
            });
        }

        data.set("__RemoteIp", ctx.header("RemoteIp"));
        data.set("__X-Forwarded-For", ctx.header("X-Forwarded-For"));
        data.set("__Proxy-Client-IP", ctx.header("Proxy-Client-IP"));
        data.set("__WL-Proxy-Client-IP", ctx.header("WL-Proxy-Client-IP"));
        data.set("__getRemoteAddr", ctx.ip());

        return data.toJson();
    }
}
