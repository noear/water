package org.noear.water.utils;

import org.noear.snack.ONode;
import org.yaml.snakeyaml.Yaml;

import java.io.StringReader;

public class ONodeUtils {
    public static ONode load(String text){
        if(TextUtils.isEmpty(text)){
            return new ONode();
        }

        int idx0 = text.indexOf(":");
        int idx1 = text.indexOf("{");
        int idx2 = text.indexOf("[");

        if(idx0 > 0){
            if(idx1 < 0){
                idx1 = 9999;
            }

            if(idx2 < 0){
                idx2 = 9999;
            }

            if(idx0 < idx1 && idx0 < idx2){
                Yaml yaml = new Yaml();
                Object tmp = yaml.load(new StringReader(text));

                return ONode.loadObj(tmp);
            }
        }

        return ONode.loadStr(text);
    }
}
