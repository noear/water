package org.noear.water;

import org.noear.solon.core.LoadBalance;

public class WaterUpstreamFactoryImp implements LoadBalance.Factory {

    @Override
    public LoadBalance create(String group, String service) {
        return WaterUpstream.get(service);
    }
}
