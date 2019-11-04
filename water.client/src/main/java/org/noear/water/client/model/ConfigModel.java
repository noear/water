package org.noear.water.client.model;

import org.noear.snack.ONode;
import org.noear.water.client.utils.RunUtil;

import java.io.StringReader;
import java.util.Properties;

/**
 * 配置模型
 * */
public class ConfigModel {
    public String tag;
    public String key;
    public String value;

    //转为String
    public String toString(){
        return  value;
    }

    /** 转为Int */
    public int toInt(){
        return Integer.parseInt(value);
    }

    /** 转为Long */
    public long toLong(){
        return Long.parseLong(value);
    }

    /** 转为Properties */
    public Properties toProp(){
        Properties tmp = new Properties();
        RunUtil.runActEx(()->tmp.load(new StringReader(value)));
        return tmp;
    }

    /** 转为ONode */
    public ONode toNode(){
        return ONode.load(value);
    }
}
