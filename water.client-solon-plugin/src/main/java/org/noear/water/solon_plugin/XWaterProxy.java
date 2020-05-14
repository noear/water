package org.noear.water.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solonclient.HttpUpstream;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.annotation.XClient;
import org.noear.water.utils.TextUtils;

public class XWaterProxy {
    public static <T> T xclient(Class<?> clz) {
        XClient c_meta = clz.getAnnotation(XClient.class);

        if (c_meta == null) {
            throw new RuntimeException("No xclient annotation");
        }

        String c_sev = c_meta.value();
        if (TextUtils.isEmpty(c_sev)) {
            throw new RuntimeException("XClient no name");
        }

        //支持 rockrpc:/rpc 模式
        if (c_sev.indexOf(":") > 0) {
            c_sev = c_sev.split(":")[0];
        }

        HttpUpstream upstream = null;
        if(XApp.cfg().isDebugMode()){
            //增加debug模式支持
            String url = System.getProperty("water.remoting-debug." + c_sev);
            if(url != null){
                upstream = (s)->url;
            }
        }

        if(upstream == null) {
            upstream = XWaterUpstream.get(c_sev);
        }

        return xclient(clz, upstream);
    }

    public static <T> T xclient(Class<?> clz, HttpUpstream upstream) {
        if (XWaterUpstream._consumer == null) {
            XWaterUpstream._consumer = "";
        }

        if (XWaterUpstream._consumer_address == null) {
            XWaterUpstream._consumer_address = "";
        }

        return new XProxy()
                .headerAdd("_from", XWaterUpstream._consumer + "@" + XWaterUpstream._consumer_address)
                .upstream(upstream)
                .create(clz);
    }
}
