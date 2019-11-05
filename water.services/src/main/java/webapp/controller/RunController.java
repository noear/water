package webapp.controller;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import waterapi.controller.cmds.*;
import waterapi.dao.db.DbApi;

import java.util.Map;


/**
 * Created by yuety on 2017/7/18.
 */
@XMapping("/run/**")
@XController
public class RunController implements XHandler {

    @Override
    public void handle(XContext c) throws Exception {
        String path = c.path();

        switch (path) {
            case "/run/push/":
                new CMD_run_push().exec(c);
                break;
            case "/run/check/": {
                DbApi.loadWhitelist(); //检测服务时，就会进行白名单刷新

                ONode data = new ONode();

                data.set("code", 1);

                c.output(data.toJson());
            }
            break;
            case "/run/whitelist/reload/": {
                ONode data = new ONode();
                try {
                    DbApi.loadWhitelist();
                    data.set("code", 1);
                    data.set("msg", "ok");
                } catch (Exception ex) {
                    data.set("code", 0);
                    data.set("msg", ex.getMessage());
                }

                c.output(data.toJson());
            }
            break;
            case "/run/check/http/": {
                ONode data = new ONode();

                Map<String, String> map = c.headerMap();
                if (map != null) {
                    map.forEach((k, v) -> {
                        data.set(k, v);
                    });
                }

                data.set("__RemoteIp", c.header("RemoteIp"));
                data.set("__X-Forwarded-For", c.header("X-Forwarded-For"));
                data.set("__Proxy-Client-IP", c.header("Proxy-Client-IP"));
                data.set("__WL-Proxy-Client-IP", c.header("WL-Proxy-Client-IP"));
                data.set("__getRemoteAddr", c.ip());

                c.output(data.toJson());
            }
            break;
            default:
                break;
        }
    }

}
