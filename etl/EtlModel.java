package watersev.dso.etl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.noear.weed.DataItem;

import java.util.Map;

public class EtlModel {
    public EtlModel(DataItem dataItem){
        this.data = dataItem;
    }
    public DataItem data;

    public String guid;
    public int __is_load;

    public Object get(String name){
        return data.get(name);
    }
    public String getString(String name){
        return data.get(name).toString();
    }

    public void setContextData(EtlContext context) throws Exception{
        if (guid == null) {
            guid = context.guid();

            context.jtSQL().setData(guid, data);
        }
    }

    public void delContextData(EtlContext context) throws Exception{
        if (guid == null) {
            return;
        }

        context.jtSQL().delData(guid);
    }

    public String buildJtSQLArgs(EtlContext context) throws Exception{
        setContextData(context);

        return "JTAPI.getData('" + guid + "')";
    }

    public static String serialize(DataItem dataItem){
        return JSON.toJSONString(dataItem.getMap(),
                SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteClassName,
                SerializerFeature.DisableCircularReferenceDetect);

    }

    public static DataItem unserialize(String text){
        //ParserConfig.getGlobalInstance().addAccept(fastjsonAccept);
        Map<String,Object> temp = (Map<String,Object>)JSON.parse(text);
        return new DataItem().setMap(temp);
    }
}
