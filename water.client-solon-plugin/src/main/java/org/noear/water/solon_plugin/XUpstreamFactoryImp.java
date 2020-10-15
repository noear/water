package org.noear.water.solon_plugin;

import org.noear.solon.core.XUpstream;

public class XUpstreamFactoryImp implements XUpstream.Factory {
    @Override
    public XUpstream create(String service) {
        return XWaterUpstream.get(service);
    }
}
