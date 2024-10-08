package org.noear.water.dso;

import org.noear.water.WaterClient;

/**
 * @author noear 2021/6/22 created
 */
public class NoticeUtils {
    public static void updateConfig(String tag, String name) {
        WaterClient.Notice.updateConfig(tag, name);
    }

    public static void updateCache(String... cacheTags) {
        WaterClient.Notice.updateCache(cacheTags);
    }

    public static void updateI18nCache(String tag, String bundle, String lang) {
        String cacheKey = String.format("i18n:%s:%s:%s", tag, bundle, lang);
        WaterClient.Notice.updateCacheByDelay(3, cacheKey);
    }

    public static String heihei(String target, String msg) {
        return WaterClient.Notice.heihei(target, msg);
    }
}
