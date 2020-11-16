package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.XHandler;

public class StartHandler implements XHandler {
    @Override
    public void handle(Context context) throws Exception {
        context.attrSet("_start", System.currentTimeMillis());
    }
}
