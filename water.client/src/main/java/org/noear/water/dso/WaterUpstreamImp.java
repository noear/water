package org.noear.water.dso;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.utils.HttpUtils;

public class WaterUpstreamImp implements WaterUpstream {
    @Override
    public HttpUtils xcall(String path) {
        return HttpUtils.http(WaterSetting.water_api_url() + path)
                .header(WW.http_header_trace, WaterClient.waterTraceId())
                .header(WW.http_header_from, WaterClient.localServiceHost());
    }
}
