package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

public class StartHandler implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        context.attrSet("_start", System.currentTimeMillis());
    }
}
