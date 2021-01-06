package org.noear.water.integration.solon;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.*;
import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.WaterProps;
import org.noear.water.WaterSetting;
import org.noear.water.annotation.Water;
import org.noear.water.annotation.WaterConfig;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.ConfigHandler;
import org.noear.water.dso.MessageHandler;
import org.noear.water.utils.TextUtils;
import org.noear.weed.xml.XmlSqlLoader;

import java.util.HashMap;
import java.util.Map;

public class XPluginImp implements Plugin {
    Map<String, MessageHandler> _router  =new HashMap<>();

    @Override
    public void start(SolonApp app) {
        XmlSqlLoader.tryLoad();

        Bridge.upstreamFactorySet(new WaterUpstreamFactoryImp());

        //尝试注册
        if (app.port() > 0) {
            if (WaterProps.service_name() != null) {
                app.plug(new WaterAdapterImp());
            }
        }

        //尝试加载配置
        if (TextUtils.isEmpty(WaterProps.service_config()) == false) {
            String[] ss = WaterProps.service_config().split(",");
            for (String s : ss) {
                String tagKey = s.trim();
                if (tagKey.startsWith("@")) {
                    tagKey = tagKey.substring(1);
                }


                String keyTmp = "*";

                if (tagKey.indexOf("/") > 0) {
                    keyTmp = tagKey.split("/")[0];
                }

                String tag = tagKey.split("/")[0];
                String key = keyTmp;

                WaterClient.Config.getProperties(tag).forEach((k, v) -> {
                    if (key.equals("*") || k.toString().indexOf(key) >= 0) {
                        Solon.cfg().put(k, v);
                    }
                });

            }
        }

        //添加Water注入支持
        Aop.context().beanInjectorAdd(Water.class, new WaterBeanInjector());

        //添加WaterMessage注解支持
        Aop.context().beanBuilderAdd(WaterMessage.class, (clz, wrap, anno) -> {
            if (MessageHandler.class.isAssignableFrom(clz)) {
                String topic = anno.value();

                if (TextUtils.isEmpty(topic) == false) {
                    _router.put(topic, wrap.raw());
                }
            }
        });

        //添加WaterConfig注解支持
        Aop.context().beanBuilderAdd(WaterConfig.class, (clz, wrap, anno) -> {
            if (ConfigHandler.class.isAssignableFrom(clz)) {
                String tag = anno.value();

                if (TextUtils.isEmpty(tag) == false) {
                    WaterClient.Config.subscribe(tag, wrap.raw());
                }
            }
        });

        //尝试加载消息订阅
        Aop.context().beanOnloaded(() -> {
            if (WaterAdapter.global() != null) {
                Aop.context().beanForeach((k, v) -> {
                    if (k.startsWith("msg:") && MessageHandler.class.isAssignableFrom(v.clz())) {
                        String topic = k.split(":")[1];

                        _router.put(topic, v.raw());
                    }
                });

                if (_router.size() > 0) {
                    WaterAdapter.global().router().putAll(_router);
                    WaterAdapter.global().messageSubscribeHandler();
                    _router.clear();
                }
            }
        });

        //改为upstream模式，可跳过nginx代理
        WaterUpstream wup = WaterUpstream.get(WW.waterapi);
        WaterSetting.water_sev_upstream(wup);

        WaterSetting.water_trace_id_supplier(() -> {
            Context ctx = Context.current();
            if (ctx == null) {
                return "";
            } else {
                String trace_id = ctx.header(WW.http_header_trace);
                if (TextUtils.isEmpty(trace_id)) {
                    trace_id = Utils.guid();
                    ctx.headerMap().put(WW.http_header_trace, trace_id);
                }

                return trace_id;
            }
        });
    }

    @Override
    public void stop() throws Throwable {
        WaterSetting.libOfDb.clear();
        WaterSetting.libOfRd.clear();
        WaterSetting.libOfCache.clear();
    }
}
