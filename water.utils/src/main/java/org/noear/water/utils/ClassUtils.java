package org.noear.water.utils;

/**
 * @author noear 2021/3/25 created
 */
public class ClassUtils {
    public static String formatClassName(String className) {
        if (TextUtils.isEmpty(className)) {
            return "";
        }

        String[] ss = className.split("\\.");

        StringBuilder sb = new StringBuilder(className.length());
        for (int i = 0, len = ss.length; i < len; i++) {
            if (i > (len - 2)) {
                sb.append(ss[i]).append('.');
            } else {
                sb.append(ss[i].charAt(0)).append('.');
            }
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
