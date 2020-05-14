package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;

public class EndHandler implements XHandler {

    @Override
    public void handle(XContext ctx) throws Exception {
        long start = ctx.attr("_start", 0l);
        long times = System.currentTimeMillis() - start;

        ctx.headerSet("_times", times + "");
    }
}
