package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.water.WaterClient;
import org.noear.water.solon_plugin.IPUtils;

public class IpHandler implements XHandler {

    @Override
    public void handle(XContext ctx) throws Exception {
        String ip = IPUtils.getIP(ctx);

        if (WaterClient.Whitelist.existsOfIp("server",  ip) == false) {
            String out = ip + " is not whitelist!";

            ctx.attrSet("output", out);
            ctx.output(out);
            ctx.setHandled(true);
        }
    }
}
