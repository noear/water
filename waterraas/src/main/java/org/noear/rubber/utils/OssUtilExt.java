package org.noear.rubber.utils;

import java.io.IOException;

public class OssUtilExt {
//    private static WaterClient.ConfigModel oss_cfg = null;
//    private static ReentrantLock run_lock = new ReentrantLock();
//    private static void tryInit() {
//        if (oss_cfg == null) {
//            run_lock.lock();
//
//            if (oss_cfg == null) {
//                oss_cfg = WaterClient.Config.get("sponge", "aliyun_oss");
//            }
//
//            run_lock.unlock();
//        }
//    }

    public static String getObject(String bucketName, String objKey) throws IOException {
        return null;

//        tryInit();
//
//        return sponge.utils.OssUtil.getObject(oss_cfg,bucketName,objKey);
    }

    public static String setObject(String bucketName, String objKey, String objVal) throws IOException {
        return null;

//        tryInit();
//
//        return sponge.utils.OssUtil.setObject(oss_cfg, bucketName, objKey, objVal);
    }
}
