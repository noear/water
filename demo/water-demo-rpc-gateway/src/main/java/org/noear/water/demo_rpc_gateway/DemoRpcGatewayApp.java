package org.noear.water.demo_rpc_gateway;

import org.noear.fairy.Fairy;
import org.noear.fairy.decoder.SnackDecoder;
import org.noear.solon.Solon;

public class DemoRpcGatewayApp {
    public static void main(String[] args) {
        Fairy.defaultDecoder = SnackDecoder.instance;

        Solon.start(DemoRpcGatewayApp.class, args);
    }
}
