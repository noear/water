package org.noear.water.client.utils;

import java.util.UUID;

public class IDUtil {
    public static String guid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
