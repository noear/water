package org.noear.water.solon_plugin;

import org.noear.solon.core.LoadBalance;

public class XUpstreamFactoryImp implements LoadBalance.Factory {
    @Override
    public LoadBalance create(String service) {
        return XWaterUpstream.get(service);
    }
}
