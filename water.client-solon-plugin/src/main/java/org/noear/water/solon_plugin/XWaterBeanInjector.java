package org.noear.water.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.FieldWrapTmp;
import org.noear.solon.core.utils.TypeUtil;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.annotation.XClient;
import org.noear.water.WaterClient;
import org.noear.water.WaterProxy;
import org.noear.water.annotation.Water;
import org.noear.water.log.WaterLogger;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;

import java.util.Properties;

/**
 * 提供water注入支持
 * */
public class XWaterBeanInjector implements BeanInjector<Water> {
    @Override
    public void handler(FieldWrapTmp fwT, Water anno) {
        //RPC client注入
        if(TextUtils.isEmpty(anno.value())){
            if (fwT.getType().isInterface()) {
                if (XApp.cfg().isDebugMode()) {
                    fwT.setValue(XWaterUpstream.xclientDebug(fwT.getType()));
                } else {
                    fwT.setValue(XWaterUpstream.xclient(fwT.getType()));
                }
            }
            return;
        }

        //日志注入
        if(fwT.getType() == WaterLogger.class){
            fwT.setValue(new WaterLogger(anno.value()));
            return;
        }

        //配置注入
        String[] tmp = anno.value().split("::");

        String tagKey = tmp[0];
        String arg = null;
        if(tmp.length > 1){
            arg = tmp[1];
        }


        if(TextUtils.isEmpty(tagKey)){
            return;
        }

        ConfigM cfg = WaterClient.Config.getByTagKey(tagKey);

        //DbContext
        if(DbContext.class.isAssignableFrom(fwT.getType())){
            fwT.setValue(cfg.getDb());
            return;
        }

        //RedisX
        if(RedisX.class.isAssignableFrom(fwT.getType())){
            if(TextUtils.isEmpty(arg)){
                fwT.setValue(cfg.getRd(0));
            }else{
                fwT.setValue(cfg.getRd(Integer.parseInt(arg)));
            }
            return;
        }

        //ICacheServiceEx
        if(ICacheServiceEx.class.isAssignableFrom(fwT.getType())) {
            String keyHeader = WaterProps.service_name();
            int defSeconds = 300;

            fwT.setValue(cfg.getCh(keyHeader, defSeconds));
            return;
        }

        //Properties
        if(Properties.class.isAssignableFrom(fwT.getType())){
            fwT.setValue(cfg.getProp());
            return;
        }

        Object val2 = TypeUtil.changeOfPop(fwT.getType(), cfg.value);
        fwT.setValue(val2);
    }
}
