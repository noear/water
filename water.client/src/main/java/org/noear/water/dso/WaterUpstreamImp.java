package org.noear.water.dso;

import org.noear.water.WaterConfig;
import org.noear.water.utils.HttpUtils;

public class WaterUpstreamImp implements WaterUpstream {
    @Override
    public HttpUtils xcall(String path) {
        return HttpUtils.http(WaterConfig.water_api_url() + path);
    }
}
