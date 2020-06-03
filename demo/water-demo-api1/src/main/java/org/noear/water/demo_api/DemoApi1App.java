package org.noear.water.demo_api;

import org.noear.solon.XApp;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.serializer.SnackSerializer;

public class DemoApi1App {
    public static void main(String[] args) {

        XProxy.defaultSerializer = new SnackSerializer();

        XApp.start(DemoApi1App.class, args);
    }
}
