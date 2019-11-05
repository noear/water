package webapp.widget;

import java.util.HashMap;
import java.util.Map;

public class MapExt extends HashMap<String,String> {
    public MapExt(Map map){
        map.forEach((k,v)->{
            this.put(k.toString(), v.toString());
        });
    }

    @Override
    public String get(Object key) {
        return get(key,"");
    }

    public String get(Object key,String def) {
        if(containsKey(key)){
            return super.get(key);
        }else{
            return def;
        }
    }

    public int getInt(String key,int def) {
        if(containsKey(key)){
            return Integer.parseInt( get(key));
        }else{
            return def;
        }
    }

    public long getLong(String key) {
        if(containsKey(key)){
            return Long.parseLong( get(key));
        }else{
            return 0L;
        }
    }

    public double getDouble(String key) {
        if(containsKey(key)){
            return Double.parseDouble( get(key));
        }else{
            return 0.0D;
        }
    }

    public boolean getBool(String key, boolean def) {
        if(containsKey(key)){
            return Boolean.parseBoolean(get(key)) ;
        }else{
            return def;
        }
    }
}
