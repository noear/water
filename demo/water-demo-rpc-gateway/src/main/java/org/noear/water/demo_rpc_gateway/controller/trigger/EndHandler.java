package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.XHandler;

public class EndHandler implements XHandler {

    @Override
    public void handle(Context ctx) throws Exception {
        long start = ctx.attr("_start", 0L);
        long times = System.currentTimeMillis() - start;

        ctx.headerSet("_times", times + "");
    }
}
