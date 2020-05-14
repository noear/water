package org.noear.water.demo_rpc_gateway;

import org.noear.solon.XApp;
import org.noear.water.solon_plugin.XWaterGateway;

public class DemoRpcGatewayApp {
    public static void main(String[] args) {
        XApp.start(DemoRpcGatewayApp.class, args, app -> {
            //注册网关
            app.http("**", new XWaterGateway());
        });
    }
}
