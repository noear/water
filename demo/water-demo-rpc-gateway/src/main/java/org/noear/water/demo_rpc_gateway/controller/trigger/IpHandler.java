package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.XHandler;
import org.noear.water.WaterClient;
import org.noear.water.solon_plugin.IPUtils;

public class IpHandler implements XHandler {

    @Override
    public void handle(Context ctx) throws Exception {
        String ip = IPUtils.getIP(ctx);

        if (WaterClient.Whitelist.existsOfServerIp(ip) == false) {
            String out = ip + " is not whitelist!";

            ctx.attrSet("output", out);
            ctx.output(out);
            ctx.setHandled(true);
        }
    }
}
