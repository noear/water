package org.noear.water.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;
import org.noear.water.WaterClient;
import org.noear.water.annotation.Water;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.utils.TextUtils;

public class XPluginImp implements XPlugin {
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

        //尝试加载消息订阅
        Aop.beanOnloaded(() -> {
            if (XWaterAdapter.global() != null) {
                Aop.beanForeach((k, v) -> {
                    if (k.startsWith("msg:") && XMessageHandler.class.isAssignableFrom(v.clz())) {
                        String msg = k.split(":")[1];

                        XWaterAdapter.global().router().put(msg, v.raw());
                    }
                });

                XWaterAdapter.global().messageSubscribeHandler();
            }
        });


        //添加WaterMessage注解支持
        Aop.factory().beanCreatorAdd(WaterMessage.class, (clz, wrap, anno) -> {
            if (XWaterAdapter.global() != null) {
                if (XMessageHandler.class.isAssignableFrom(clz)) {
                    String topic = anno.value();

                    XWaterAdapter.global().router().put(topic, wrap.raw());
                    XWaterAdapter.global().messageSubscribeTopic(topic);
                }
            }
        });

        //添加Water注解支持
        Aop.factory().beanInjectorAdd(Water.class, new XWaterBeanInjector());
    }
}
