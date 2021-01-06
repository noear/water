package org.noear.water.integration.springboot;

import org.noear.nami.Nami;
import org.noear.nami.NamiException;
import org.noear.nami.annotation.NamiClient;
import org.noear.solon.Utils;
import org.noear.solon.extend.springboot.SpringBootLinkSolon;
import org.noear.water.annotation.Water;
import org.noear.water.integration.solon.WaterBeanInjector;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author noear 2021/1/6 created
 */
@SpringBootLinkSolon
@Configuration
public class AutoConfiguration extends InstantiationAwareBeanPostProcessorAdapter {
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        Class<?> beanClz = bean.getClass();

        ReflectionUtils.doWithFields(beanClz, (field -> {
            Water anno = field.getAnnotation(Water.class);

            if (anno != null) {
                if (field.getType().isInterface()) {
                    postAnno(anno, field, bean);
                }
            }
        }));

        return pvs;
    }

    private void postAnno(Water anno, Field field, Object bean) {
        VarHolderOfField vh = new VarHolderOfField(bean.getClass(), field, anno);
        WaterBeanInjector.instance.doInject(vh, anno);
    }
}
