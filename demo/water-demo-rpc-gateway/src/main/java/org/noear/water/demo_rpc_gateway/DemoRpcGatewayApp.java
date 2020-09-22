package org.noear.water.demo_rpc_gateway;

import org.noear.fairy.Fairy;
import org.noear.fairy.decoder.SnackDecoder;
import org.noear.solon.XApp;

public class DemoRpcGatewayApp {
    public static void main(String[] args) {
        Fairy.defaultDecoder = SnackDecoder.instance;

        XApp.start(DemoRpcGatewayApp.class, args);
    }
}
