package org.noear.water.utils;

public class TextUtils {
    public static boolean isEmpty(CharSequence str){
        return (str == null || str.length()==0);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean equals(String cs1, String cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            if (cs1.length() != cs2.length()) {
                return false;
            } else {
                return cs1.equals(cs2);
            }
        } else {
            return false;
        }
    }
}
