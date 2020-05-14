package org.noear.water.demo_rpc_gateway.controller.trigger;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;

public class StartHandler implements XHandler {
    @Override
    public void handle(XContext context) throws Exception {
        context.attrSet("_start", System.currentTimeMillis());
    }
}
