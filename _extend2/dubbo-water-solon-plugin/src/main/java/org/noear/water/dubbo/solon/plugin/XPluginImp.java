package org.noear.water.dubbo.solon.plugin;

import org.noear.solon.Solon;
import org.noear.solon.core.Plugin;

public class XPluginImp implements Plugin {
    @Override
    public void start(Solon app) {
        WaterRegistryLib.start();
    }

    @Override
    public void stop() throws Throwable {
        WaterRegistryLib.stop();
        System.out.println("Water-dubbo-solon-plugin stop");
    }
}
