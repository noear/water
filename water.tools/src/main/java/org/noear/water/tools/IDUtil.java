package org.noear.water.tools;

import java.util.UUID;

public class IDUtil {
    public static String guid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
