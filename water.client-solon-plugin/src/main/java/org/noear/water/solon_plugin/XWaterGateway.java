package org.noear.water.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import org.noear.solon.core.XMap;
import org.noear.solon.core.XRender;
import org.noear.solonclient.HttpUpstream;
import org.noear.solonclient.XProxy;

import java.util.HashMap;
import java.util.Map;

/*
* Water Gateway
* */
public class XWaterGateway implements XHandler, XRender {
    Map<String,HttpUpstream> router = new HashMap<>();

    public XWaterGateway() {
        XMap map = XApp.cfg().getXmap("water.remoting");

        if (XApp.cfg().isDebugMode()) {
            map.forEach((alias, service) -> {
                String url = System.getProperty("water.remoting-debug." + service);
                add(alias, (s) -> url);
            });
        } else {
            map.forEach((alias, service) -> {
                add(alias, service);
            });
        }
    }

    protected void add(String alias,String service){
        router.put(alias,  XWaterUpstream.get(service));
    }

    protected void add(String alias, HttpUpstream upstream){
        router.put(alias, upstream);
    }

    @Override
    public void handle(XContext ctx) throws Throwable {

        String path = ctx.path();
        //长度不足3的，说明不够两段
        if(path.length()<3){
            ctx.status(404);
            return;
        }

        String[] paths = path.substring(1).split("/");
        //不够两段的肯定不是正常地址
        if(paths.length<2){
            ctx.status(404);
            return;
        }

        String alias = paths[0];
        HttpUpstream upstream = router.get(alias); //第1段为sev

        //如果没有预配的负载不干活
        if (upstream == null) {
            ctx.status(404);
            return;
        }

        //找到 fun 部分
        int idx = path.indexOf("/", 2) + 1;
        String fun = path.substring(idx);

        String rst = new XProxy(null)
                .url(upstream.getTarget(alias), fun)
                .call(ctx.headerMap(), ctx.paramMap())
                .getString();

        render(rst, ctx);
    }

    /** XRender,是为了进一步可以重载控制 */
    @Override
    public void render(Object obj, XContext ctx) throws Throwable {
        ctx.output(obj.toString());
    }
}
