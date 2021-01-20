package org.noear.water.utils;

public class JsondUtils {
    private static final String key = "j$6gxA^KJgBiOgco";

    public static String encode(String json) {
        return EncryptUtils.aesEncrypt(json, key, null);
    }

    public static String decode(String jsonD) {
        return EncryptUtils.aesDecrypt(jsonD, key, null);
    }
}
