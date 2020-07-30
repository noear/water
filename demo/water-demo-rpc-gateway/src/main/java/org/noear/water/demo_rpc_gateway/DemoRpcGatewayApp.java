package org.noear.water.demo_rpc_gateway;

import org.noear.solon.XApp;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.serializer.SnackSerializerD;

public class DemoRpcGatewayApp {
    public static void main(String[] args) {
        XProxy.defaultDeserializer = SnackSerializerD.instance;

        XApp.start(DemoRpcGatewayApp.class, args);
    }
}
