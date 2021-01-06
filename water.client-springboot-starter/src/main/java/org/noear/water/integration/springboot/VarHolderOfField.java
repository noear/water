package org.noear.water.integration.springboot;

import org.noear.solon.core.VarHolder;
import org.noear.solon.core.wrap.FieldWrap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @author noear 2021/1/6 created
 */
public class VarHolderOfField implements VarHolder {
    protected final FieldWrap fw;
    protected final Object obj;

    public VarHolderOfField(Class clz, Field field, Object obj) {
        FieldWrap fw = new FieldWrap(clz, field);
        this.fw = fw;
        this.obj = obj;
    }
    public VarHolderOfField(FieldWrap fw, Object obj) {
        this.fw = fw;
        this.obj = obj;
    }

    @Override
    public ParameterizedType getGenericType() {
        return fw.genericType;
    }

    /**
     * 获取字段类型
     * */
    @Override
    public Class<?> getType(){
        return fw.type;
    }

    /**
     * 获取所有注解
     * */
    @Override
    public Annotation[] getAnnoS(){
        return fw.annoS;
    }

    /**
     * 设置字段的值
     */
    @Override
    public void setValue(Object val) {
        fw.setValue(obj, val);
    }
}
