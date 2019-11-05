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
        if(value == null){
            return 0;
        }

        return Integer.parseInt(value);
    }

    /** 转为Long */
    public long toLong(){
        if(value == null){
            return 0;
        }

        return Long.parseLong(value);
    }

    /** 转为Properties */
    public Properties toProp(){
        Properties tmp = new Properties();

        if(value == null){
            return tmp;
        }

        RunUtil.runActEx(()->tmp.load(new StringReader(value)));
        return tmp;
    }

    /** 转为ONode */
    public ONode toNode(){
        if(value == null){
            return new ONode();
        }

        return ONode.load(value);
    }
}
