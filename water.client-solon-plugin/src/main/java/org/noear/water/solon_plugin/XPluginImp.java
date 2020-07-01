package org.noear.water.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;
import org.noear.water.WaterClient;
import org.noear.water.annotation.Water;
import org.noear.water.annotation.WaterConfig;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.ConfigHandler;
import org.noear.water.dso.MessageHandler;
import org.noear.water.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class XPluginImp implements XPlugin {
    Map<String, MessageHandler> _router  =new HashMap<>();
    @Override
    public void start(XApp app) {

        //尝试注册
        if (app.port() > 0) {
            if (WaterProps.service_name() != null) {
                app.plug(new XWaterAdapterImp());
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
                        XApp.cfg().put(k, v);
                    }
                });

            }
        }

        //添加Water注入支持
        Aop.factory().beanInjectorAdd(Water.class, new XWaterBeanInjector());

        //添加WaterMessage注解支持
        Aop.factory().beanCreatorAdd(WaterMessage.class, (clz, wrap, anno) -> {
            if (MessageHandler.class.isAssignableFrom(clz)) {
                String topic = anno.value();

                if (TextUtils.isEmpty(topic) == false) {
                    _router.put(topic,wrap.raw());
                }
            }
        });

        //添加WaterConfig注解支持
        Aop.factory().beanCreatorAdd(WaterConfig.class, (clz, wrap, anno) -> {
            if (ConfigHandler.class.isAssignableFrom(clz)) {
                String tag = anno.value();

                if (TextUtils.isEmpty(tag) == false) {
                    WaterClient.Config.subscribe(tag,wrap.raw());
                }
            }
        });

        //尝试加载消息订阅
        Aop.beanOnloaded(() -> {
            if (XWaterAdapter.global() != null) {
                Aop.beanForeach((k, v) -> {
                    if (k.startsWith("msg:") && XMessageHandler.class.isAssignableFrom(v.clz())) {
                        String topic = k.split(":")[1];

                        _router.put(topic, v.raw());
                    }
                });

                if(_router.size() > 0) {
                    XWaterAdapter.global().router().putAll(_router);
                    XWaterAdapter.global().messageSubscribeHandler();
                    _router.clear();
                }
            }
        });
    }

    @Override
    public void stop() throws Throwable {
        org.noear.water.WaterConfig.libOfDb.clear();
        org.noear.water.WaterConfig.libOfRd.clear();
        org.noear.water.WaterConfig.libOfCache.clear();
    }
}
