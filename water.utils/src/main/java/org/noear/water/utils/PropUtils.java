package org.noear.water.utils;


import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class PropUtils {

    public static Properties build(String text) {
        try {
            return build0(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties build0(String text) throws IOException {
        if(TextUtils.isEmpty(text)){
            return new Properties();
        }

        text = text.trim();

        int idx1 = text.indexOf("=");
        int idx2 = text.indexOf(":");

        //有{开头
        if (text.startsWith("{") && text.endsWith("}")) {
            PropertiesJson tmp = new PropertiesJson();
            tmp.loadJson(text);
            return tmp;
        }

        //有[开头
        if (text.startsWith("[") && text.endsWith("]")) {
            PropertiesJson tmp = new PropertiesJson();
            tmp.loadJson(text);
            return tmp;
        }


        //有=
        if (idx1 > 0 && (idx1 < idx2 || idx2 < 0)) {
            Properties tmp = new Properties();
            tmp.load(new StringReader(text));
            return tmp;
        }

        //有:
        if (idx2 > 0 && (idx2 < idx1 || idx1 < 0)) {
            PropertiesYaml tmp = new PropertiesYaml();
            tmp.loadYml(new StringReader(text));
            return tmp;
        }

        return new Properties();
    }
}
