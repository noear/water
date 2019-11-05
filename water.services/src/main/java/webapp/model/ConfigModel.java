package webapp.model;

import org.noear.snack.ONode;
import org.noear.water.tools.RunUtil;

import java.io.StringReader;
import java.util.Date;
import java.util.Properties;

public class ConfigModel {
    public String tag;
    public String key;
    public String value;
    public Date update_fulltime;



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
