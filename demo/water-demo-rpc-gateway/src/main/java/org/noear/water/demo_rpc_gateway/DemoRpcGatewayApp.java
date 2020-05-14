package org.noear.water.demo_rpc_gateway;

import org.noear.solon.XApp;
import org.noear.solon.core.XMethod;
import org.noear.water.demo_rpc_gateway.controller.trigger.EndHandler;
import org.noear.water.demo_rpc_gateway.controller.trigger.IpHandler;
import org.noear.water.demo_rpc_gateway.controller.trigger.StartHandler;
import org.noear.water.solon_plugin.XWaterGateway;

public class DemoRpcGatewayApp {
    public static void main(String[] args) {
        XApp.start(DemoRpcGatewayApp.class, args,(app)->{
            app.http("**",new XWaterGateway());

            app.before("**", XMethod.HTTP,new StartHandler());
            app.before("**", XMethod.HTTP,new IpHandler());
            app.after("**", XMethod.HTTP,new EndHandler());
        });
    }
}
