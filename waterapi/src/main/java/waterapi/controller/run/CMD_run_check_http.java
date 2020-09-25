package waterapi.controller.run;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;

import java.util.Map;

@XController
public class CMD_run_check_http extends UapiBase {
    @XMapping("/run/check/http/")
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
