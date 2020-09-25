package waterapi.controller.run;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import waterapi.controller.UapiBase;

import java.util.Map;

/**
 * 运行检测HTTP
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
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
