package org.noear.water.utils;

import org.noear.snack.ONode;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author noear 2021/10/19 created
 */
public class LogHelper {
    public static String contentAsString(Object content) {
        if (content != null) {
            if (content instanceof String) {
                //处理字符串
                return (String) content;
            } else if (content instanceof Throwable) {
                //处理异常
                StringWriter sw = new StringWriter();
                ((Throwable) content).printStackTrace(new PrintWriter(sw));
                return sw.toString();
            } else {
                //处理其它对象（进行json）
                return ONode.load(content).toJson();
            }
        }

        return null;
    }
}
