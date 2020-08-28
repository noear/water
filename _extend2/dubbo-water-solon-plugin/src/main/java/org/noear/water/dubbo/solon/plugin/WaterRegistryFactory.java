package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;

public class WaterRegistryFactory extends AbstractRegistryFactory {
    @Override
    protected Registry createRegistry(URL url) {
        return new WaterRegistry(url);
    }
}
