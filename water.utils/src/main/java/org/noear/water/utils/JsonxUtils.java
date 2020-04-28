package org.noear.water.utils;

public class JsonxUtils {
    private static final String key = "j$6gxA^KJgBiOgco";

    public static String encode(String json) {
        return EncryptUtils.aesEncrypt(json, key, null);
    }

    public static String decode(String jsonX) {
        return EncryptUtils.aesDecrypt(jsonX, key, null);
    }
}
