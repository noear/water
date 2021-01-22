package org.noear.water;

/**
 * Water 内部服务地址
 *
 * @author noear
 * @since 2.0
 */
public class WaterAddress {
    private static String configApiUrl = WaterSetting.water_api_url();
    private static String messageApiUrl = WaterSetting.water_api_url();
    private static String logApiUrl = WaterSetting.water_api_url();
    private static String registryApiUrl = WaterSetting.water_api_url();
    private static String trackApiUrl = WaterSetting.water_api_url();
    private static String noticeApiUrl = WaterSetting.water_api_url();
    private static String whitelistApiUrl = WaterSetting.water_api_url();

    public static String getConfigApiUrl() {
        return configApiUrl;
    }

    public static void setConfigApiUrl(String url) {
        configApiUrl = url;
    }

    public static String getMessageApiUrl() {
        return messageApiUrl;
    }

    public static void setMessageApiUrl(String url) {
        messageApiUrl = url;
    }

    public static String getLogApiUrl() {
        return logApiUrl;
    }

    public static void setLogApiUrl(String url) {
        logApiUrl = url;
    }

    public static String getRegistryApiUrl() {
        return registryApiUrl;
    }

    public static void setRegistryApiUrl(String url) {
        registryApiUrl = url;
    }

    public static String getTrackApiUrl() {
        return trackApiUrl;
    }

    public static void setTrackApiUrl(String url) {
        trackApiUrl = url;
    }

    public static String getNoticeApiUrl() {
        return noticeApiUrl;
    }

    public static void setNoticeApiUrl(String url) {
        noticeApiUrl = url;
    }

    public static String getWhitelistApiUrl() {
        return whitelistApiUrl;
    }

    public static void setWhitelistApiUrl(String url) {
        whitelistApiUrl = url;
    }
}
