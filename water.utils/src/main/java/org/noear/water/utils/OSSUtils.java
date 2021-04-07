package org.noear.water.utils;

import org.noear.water.utils.hdfs.HdfsService;

import java.io.File;

public class OSSUtils {
    static HdfsService service;

    public static void setService(HdfsService service) {
        if (service != null) {
            OSSUtils.service = service;
        }
    }

    public static String getObj(String key) throws Exception {
        return service.getObj(key);
    }

    public static String putObj(String key, String content) throws Exception {
        return service.putObj(key, content);
    }

    public static String putObj(String key, File file) throws Exception {
        return service.putObj(key, file);
    }
}
