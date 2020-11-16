package org.noear.water.demo_rpc_gateway.controller;

import org.noear.solon.annotation.After;
import org.noear.solon.annotation.Before;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.water.demo_rpc_gateway.controller.trigger.EndHandler;
import org.noear.water.demo_rpc_gateway.controller.trigger.IpHandler;
import org.noear.water.demo_rpc_gateway.controller.trigger.StartHandler;
import org.noear.water.solon_plugin.XWaterGateway;

@Before({StartHandler.class, IpHandler.class})
@After({EndHandler.class})
@Mapping("**")
@Controller
public class ApiGateway extends XWaterGateway {
}
