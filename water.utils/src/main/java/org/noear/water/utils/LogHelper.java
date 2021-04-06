package org.noear.water.utils;

import org.noear.snack.ONode;

/**
 * @author noear 2021/4/6 created
 */
public class LogHelper {
    public static String contentAsString(Object content) {
        if (content != null) {
            if (content instanceof String) {
                //处理字符串
                return (String) content;
            } else if (content instanceof Throwable) {
                //处理异常
                return ThrowableUtils.getString((Throwable) content);
            } else {
                //处理其它对象（进行json）
                return ONode.load(content).toJson();
            }
        }

        return null;
    }
}
