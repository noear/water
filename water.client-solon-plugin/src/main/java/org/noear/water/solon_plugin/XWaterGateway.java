package org.noear.water.solon_plugin;

import org.noear.fairy.Fairy;
import org.noear.fairy.Result;
import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.Upstream;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.utils.TextUtils;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/*
* Water Gateway
* */
public class XWaterGateway implements Handler {
    Map<String, Upstream> router = new HashMap<>();

    public XWaterGateway() {
        NvMap map = Solon.cfg().getXmap("water.gateway");

        map.forEach((service, alias) -> {
            if (Solon.cfg().isDebugMode()) {
                //增加debug模式支持
                String url = System.getProperty("water.remoting-debug." + service);
                if (url != null) {
                    add(alias, () -> url);
                    return;
                }
            }
            if (router.containsKey(alias) == false) {
                add(alias, service);
            }
        });
    }

    protected void add(String alias, String service) {
        router.put(alias, XWaterUpstream.get(service));
    }

    protected void add(String alias, Upstream upstream) {
        router.put(alias, upstream);
    }

    @Override
    public void handle(Context ctx) throws Throwable {

        String path = ctx.path();
        //长度不足3的，说明不够两段
        if (path.length() < 3) {
            ctx.status(404);
            return;
        }

        String fun = null;

        //尝试去header 取 alias
        String alias = ctx.header("_service");
        if (TextUtils.isEmpty(alias)) {
            //尝试去param 取 alias
            alias = ctx.param("_service");
        }

        if (TextUtils.isEmpty(alias)) {
            //尝试去path 取 alias
            String[] paths = path.substring(1).split("/");
            //不够两段的肯定不是正常地址
            if (paths.length < 2) {
                ctx.status(404);
                return;
            }

            alias = paths[0];
            //找到 fun 部分
            int idx = path.indexOf("/", 2) + 1;
            fun = path.substring(idx);
        } else {
            fun = path;
        }

        Upstream upstream = router.get(alias); //第1段为sev

        //如果没有预配的负载不干活
        if (upstream == null) {
            ctx.statusSet(404);
            return;
        }

        Result rst = new Fairy()
                .url(upstream.getServer(), fun)
                .call(headers(ctx), ctx.paramMap())
                .result();

        renderDo(rst, ctx);
    }

    /**
     * 用于添加头
     */
    protected Map<String, String> headers(Context ctx) {
        return null;
    }

    /**
     * XRender,是为了进一步可以重载控制
     */
    protected void renderDo(Result rst, Context ctx) throws Throwable {
        rst.headers().forEach(kv -> {
            ctx.headerSet(kv.getKey(), kv.getValue());
        });

        ctx.output(new ByteArrayInputStream(rst.body()));
    }
}
