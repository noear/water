package org.noear.water.demo_rpc_gateway.controller;

import org.noear.solon.annotation.XAfter;
import org.noear.solon.annotation.XBefore;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.demo_rpc_gateway.controller.trigger.EndHandler;
import org.noear.water.demo_rpc_gateway.controller.trigger.IpHandler;
import org.noear.water.demo_rpc_gateway.controller.trigger.StartHandler;
import org.noear.water.solon_plugin.XWaterGateway;

@XBefore({StartHandler.class, IpHandler.class})
@XAfter({EndHandler.class})
@XMapping("**")
@XController
public class ApiGateway extends XWaterGateway {
}
