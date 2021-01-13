package org.noear.water.dso;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.utils.HttpUtils;

/**
 * Water 负载器
 *
 * @author noear
 * @since 2.0
 * */
public class UpstreamImp implements Upstream {
    @Override
    public String getServer() {
        return WaterSetting.water_api_url();
    }

    @Override
    public HttpUtils http(String path) {
        return HttpUtils.http(getServer() + path)
                .header(WW.http_header_trace, WaterClient.waterTraceId())
                .header(WW.http_header_from, WaterClient.localServiceHost());
    }
}
