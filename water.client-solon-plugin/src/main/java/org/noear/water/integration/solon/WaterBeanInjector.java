package org.noear.water.integration.solon;

import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.VarHolder;
import org.noear.solon.core.util.ConvertUtil;
import org.noear.water.WaterClient;
import org.noear.water.WaterProps;
import org.noear.water.WaterSetting;
import org.noear.water.annotation.Water;
import org.noear.water.log.WaterLogger;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.cache.SecondCache;

import java.util.Properties;

/**
 * 提供water注入支持
 * */
public class WaterBeanInjector implements BeanInjector<Water> {
    public static final WaterBeanInjector instance = new WaterBeanInjector();

    @Override
    public void doInject(VarHolder varH, Water anno) {
        Object val2 = build(varH.getType(), anno);

        if (val2 != null) {
            varH.setValue(val2);
        }
    }

    public Object build(Class<?> type, Water anno) {
        //RPC client注入
        if(TextUtils.isEmpty(anno.value())) {
            if (type.isInterface()) {
                return (WaterUpstream.client(type));
            }
            return null;
        }

        //日志注入
        if(type == WaterLogger.class) {
            return (WaterLogger.get(anno.value()));
        }

        //配置注入
        String[] tmp = anno.value().split("::");

        String tagKey = tmp[0];
        String arg = null;
        if(tmp.length > 1){
            arg = tmp[1];
        }


        if(TextUtils.isEmpty(tagKey)){
            return null;
        }

        ConfigM cfg = WaterClient.Config.getByTagKey(tagKey);

        //DbContext
        if(DbContext.class.isAssignableFrom(type)){
            DbContext db = WaterSetting.libOfDb.get(cfg.value);
            if(db == null){
                db = cfg.getDb(true);
                WaterSetting.libOfDb.put(cfg.value,db);
            }

            return db;
        }

        //RedisX
        if(RedisX.class.isAssignableFrom(type)){
            String key = cfg.value +"_"+arg;

            RedisX rdx = WaterSetting.libOfRd.get(key);
            if(rdx == null){
                if(TextUtils.isEmpty(arg)){
                    rdx = cfg.getRd();
                }else{
                    rdx = cfg.getRd(Integer.parseInt(arg));
                }

                WaterSetting.libOfRd.put(key,rdx);
            }

            return rdx;
        }

        //ICacheServiceEx
        if(ICacheServiceEx.class.isAssignableFrom(type)) {
            ICacheServiceEx cache = WaterSetting.libOfCache.get(cfg.value);

            if (cache == null) {
                String keyHeader = WaterProps.service_name();
                int defSeconds = 300;

                ICacheServiceEx cache1 = new LocalCache(defSeconds);
                ICacheServiceEx cache2 = cfg.getCh(keyHeader, defSeconds);
                cache = new SecondCache(cache1, cache2);

                WaterSetting.libOfCache.put(cfg.value, cache);
            }

            return cache;
        }

        //Properties
        if(Properties.class.isAssignableFrom(type)){
             return cfg.getProp();
        }

        Object val2 = ConvertUtil.to(type, cfg.value);
        return val2;
    }
}
