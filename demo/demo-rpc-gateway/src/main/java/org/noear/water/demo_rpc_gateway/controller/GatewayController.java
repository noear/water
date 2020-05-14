package org.noear.water.demo_rpc_gateway.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.solon_plugin.XWaterGateway;

@XMapping("**")
@XController
public class GatewayController extends XWaterGateway {

}
