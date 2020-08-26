package org.noear.water.solon_plugin;

import org.noear.solon.core.XUpstream;
import org.noear.solon.core.XUpstreamFactory;

public class XUpstreamFactoryImp implements XUpstreamFactory {
    @Override
    public XUpstream create(String service) {
        return XWaterUpstream.get(service);
    }
}
