package org.noear.water.solon_plugin;

import org.noear.solon.Solon;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;

public class XWaterAdapterImp extends XWaterAdapter {

    private String _msg_receiver_url = null;

    public XWaterAdapterImp() {
        String host = WaterProps.service_hostname();

        if (TextUtils.isEmpty(host)) {
            return;
        }

        String host_old = host;
        if (host.startsWith("@")) {
            host = WaterClient.Config.getByTagKey(host.substring(1)).value;
        }

        host = host.trim();

        if (TextUtils.isEmpty(host)) {
            throw new RuntimeException("Configuration " + host_old + " could not be found");
        }

        if (host.indexOf("://") < 0) {
            host = "http://" + host;
        }

        if (host.endsWith("/")) {
            _msg_receiver_url = host + "msg/receive";
        } else {
            _msg_receiver_url = host + "/msg/receive";
        }
    }

    @Override
    public String msg_receiver_url() {
        return _msg_receiver_url;
    }

    @Override
    public String alarm_mobile() {
        return WaterProps.service_alarm();
    }

    @Override
    public String service_name() {
        return WaterProps.service_name();
    }

    @Override
    public String service_tag() {
        return WaterProps.service_tag();
    }

    @Override
    public String service_secretKey() {
        return WaterProps.service_secretKey();
    }


}
