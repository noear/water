package waterapi.controller._run;

import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import waterapi.controller.UapiBase;

import java.util.Map;

/**
 * 运行检测HTTP
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Controller
public class CMD_run_check_http extends UapiBase {
    @Mapping("/_run/check/http/")
    public String run_check_http(Context ctx) {
        ONode data = new ONode();

        ctx.headerMap().forEach(kv -> {
            data.set(kv.getKey(), kv.getFirstValue());
        });

        data.set("__RemoteIp", ctx.header("RemoteIp"));
        data.set("__X-Forwarded-For", ctx.header("X-Forwarded-For"));
        data.set("__Proxy-Client-IP", ctx.header("Proxy-Client-IP"));
        data.set("__WL-Proxy-Client-IP", ctx.header("WL-Proxy-Client-IP"));
        data.set("__getRemoteAddr", ctx.remoteIp());

        return data.toJson();
    }
}
