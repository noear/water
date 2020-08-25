package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;

public class WaterRegistryFactory implements RegistryFactory {
    @Override
    public Registry getRegistry(URL url) {
        return new WaterRegistry(url);
    }
}
