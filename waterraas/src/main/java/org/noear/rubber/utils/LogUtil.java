package org.noear.rubber.utils;

import org.noear.water.WaterClient;
import org.noear.water.log.Level;

public class LogUtil {


    public static void write(String tag, String label, String content) {
        write(tag, null, label, content);
    }

    public static void write(String tag, String tag1, String label, String content) {
        WaterClient.Log.append("water_log_raas", Level.INFO, tag, tag1, label, content);

        System.out.print(tag + "::\r\n");
        System.out.print(content);
        System.out.print("\r\n");
    }

    public static void debug(String tag, String tag2, String label, String txt) {
        if (tag2 == null) {
            tag2 = "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(txt);

        WaterClient.Log.append("water_log_raas_debug", Level.DEBUG, tag, "", tag2, label, sb.toString());

        System.out.print(tag + "::\r\n");
        System.out.print(sb.toString());
        System.out.print("\r\n");
    }
}
