package org.noear.water.demo_rpc_gateway;

import org.noear.solon.XApp;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.serializer.SnackSerializer;

public class DemoRpcGatewayApp {
    public static void main(String[] args) {
        XProxy.defaultSerializer = new SnackSerializer();

        XApp.start(DemoRpcGatewayApp.class, args);
    }
}
