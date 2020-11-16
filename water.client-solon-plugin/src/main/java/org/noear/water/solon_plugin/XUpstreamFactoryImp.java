package org.noear.water.solon_plugin;

import org.noear.solon.core.Upstream;

public class XUpstreamFactoryImp implements Upstream.Factory {
    @Override
    public Upstream create(String service) {
        return XWaterUpstream.get(service);
    }
}
