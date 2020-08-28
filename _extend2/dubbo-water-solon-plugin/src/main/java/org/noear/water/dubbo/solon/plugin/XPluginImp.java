package org.noear.water.dubbo.solon.plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.XPlugin;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {
        WaterRegistryLib.start();
    }

    @Override
    public void stop() throws Throwable {
        WaterRegistryLib.stop();
        System.out.println("Water-dubbo-solon-plugin stop");
    }
}
